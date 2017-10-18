package de.canitzp.amcm.editor;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import de.canitzp.amcm.*;
import de.canitzp.amcm.converter.AMCMWriter;
import net.minecraft.util.EnumFacing;
import org.apache.commons.io.FileUtils;
import scala.Int;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class AMCMEditor {

    public static int FRAME_WIDTH = 1511;
    public static int FRAME_HEIGTH = 850;

    public static List<IAMCMShapes> modelShapes = new ArrayList<>();
    private static AMCMModel actualModel = null;

    private static BufferedImage currentTexture = null;
    private static EnumFacing currenttextureFacing = EnumFacing.NORTH;
    private static JLabel facingLabel = new JLabel(currenttextureFacing.getName());

    private static File saveFile = null;
    private static JList<IAMCMShapes> shapes = new JList<>();
    private static JLabel infoLabel = new JLabel(" ");

    public static void main(String[] args){
        for(int i = 0; i < args.length; i++){
            String s = args[i];
            if("--w".equals(s)){
                FRAME_WIDTH = Integer.parseInt(args[i+1]);
                i++;
            }
            if("--h".equals(s)){
                FRAME_HEIGTH = Integer.parseInt(args[i+1]);
                i++;
            }
        }
        if(Toolkit.getDefaultToolkit().getScreenSize().getWidth() < FRAME_WIDTH || Toolkit.getDefaultToolkit().getScreenSize().getHeight() < FRAME_HEIGTH){
            JOptionPane.showMessageDialog(null, "The Screen resolution needs to be at least "+FRAME_WIDTH+"x"+FRAME_HEIGTH+" to launch the Editor!");
            throw new RuntimeException("The Screen resolution needs to be at least "+FRAME_WIDTH+"x"+FRAME_HEIGTH+" to launch the Editor!");
        }
        try {
            UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());
        } catch (Exception ignored) {}
        JFrame frame = new JFrame("Advanced Minecraft Model Editor");
        frame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        frame.setMaximumSize(frame.getMinimumSize());
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        initParts(frame);
        frame.add(new JScrollPane(shapes), BorderLayout.WEST);
        infoLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel infoTextPanel = getCentered(infoLabel);
        infoTextPanel.setBackground(Color.WHITE);
        infoTextPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        frame.add(infoTextPanel, BorderLayout.SOUTH);
        shapes.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof AMCMDefaultShape){
                    setText(((AMCMDefaultShape) value).name);
                }
                return c;
            }
        });
        shapes.addListSelectionListener(e -> updateShapePanel(shapes.getSelectedValue()));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void initParts(JFrame frame){
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileItemNew = new JMenuItem("New");
        fileItemNew.addActionListener(e -> System.out.println("New"));
        fileMenu.add(fileItemNew);
        JMenuItem fileItemOpen = new JMenuItem("Open");
        fileItemOpen.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(AMCMEditor.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            chooser.setFileFilter(new FileNameExtensionFilter("AMCModel (.amcm)", "amcm"));
            if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
                File model = chooser.getSelectedFile();
                if(model != null){
                    try {
                        saveFile = model;
                        modelShapes.clear();
                        actualModel = AdvancedMinecraftModel.loadModel(FileUtils.openInputStream(saveFile));
                        actualModel.createModel(modelShapes);
                        updateListModel();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        fileMenu.add(fileItemOpen);
        JMenuItem fileItemSave = new JMenuItem("Save");
        fileItemSave.addActionListener(e -> saveFile(frame, false));
        fileMenu.add(fileItemSave);
        JMenuItem fileItemSaveAs = new JMenuItem("Save as");
        fileItemSaveAs.addActionListener(e -> saveFile(frame, true));
        fileMenu.add(fileItemSaveAs);
        bar.add(fileMenu);

        JToolBar shapeManBar = new JToolBar(SwingConstants.HORIZONTAL);
        shapeManBar.add(new AbstractAction("New Box") {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelShapes.add(new AMCMBox(1, 1, 1).setName("box_" + (modelShapes.size() + 1)));
                updateListModel();
            }
        });
        shapeManBar.addSeparator();
        shapeManBar.add(new AbstractAction("Save (No file writing)") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sel = shapes.getSelectedIndex();
                if(sel > -1) {
                    IAMCMShapes shape = shapes.getModel().getElementAt(sel);
                    saveShape(shape);
                    updateShapePanel(shape);
                }
            }
        });
        shapeManBar.addSeparator();
        shapeManBar.add(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sel = shapes.getSelectedIndex();
                if(sel > -1){
                    IAMCMShapes shape = shapes.getModel().getElementAt(sel);
                    String name;
                    if(shape instanceof AMCMDefaultShape){
                        name = ((AMCMDefaultShape) shape).name;
                    } else name = shape.toString();
                    if(JOptionPane.showConfirmDialog(frame, "Do you really want to delete '" + name + "'?") == JOptionPane.YES_OPTION){
                        modelShapes.remove(sel);
                        updateListModel();
                    }
                }
            }
        });

        frame.setJMenuBar(bar);
        frame.add(shapeManBar, BorderLayout.BEFORE_FIRST_LINE);

        JTabbedPane shapeAttributes = new JTabbedPane();

        JPanel attributes = new JPanel();
        attributes.setLayout(new BoxLayout(attributes, BoxLayout.Y_AXIS));

        shapeType.setEnabled(false);
        attributes.add(getWithLabel("Shape type: ", shapeType));
        shapeWidth.setEnabled(false);
        shapeHeight.setEnabled(false);
        shapeDepth.setEnabled(false);
        shapeOffsetX.setEnabled(false);
        shapeOffsetY.setEnabled(false);
        shapeOffsetZ.setEnabled(false);
        shapeRotPointX.setEnabled(false);
        shapeRotPointY.setEnabled(false);
        shapeRotPointZ.setEnabled(false);
        shapeRotAngleX.setEnabled(false);
        shapeRotAngleY.setEnabled(false);
        shapeRotAngleZ.setEnabled(false);
        attributes.add(wrapWithNamedBorder("Dimension:", getWithLabel("Width: ", shapeWidth),
                getWithLabel("Height: ", shapeHeight), getWithLabel("Depth: ", shapeDepth)));
        attributes.add(wrapWithNamedBorder("Offset:", getWithLabel("x: ", shapeOffsetX),
                getWithLabel("y: ", shapeOffsetY), getWithLabel("z: ", shapeOffsetZ)));
        attributes.add(wrapWithNamedBorder("Rotation Point:", getWithLabel("x: ", shapeRotPointX),
                getWithLabel("y: ", shapeRotPointY), getWithLabel("z: ", shapeRotPointZ)));
        attributes.add(wrapWithNamedBorder("Rotation Angle:", getWithLabel("x: ", shapeRotAngleX),
                getWithLabel("y: ", shapeRotAngleY), getWithLabel("z: ", shapeRotAngleZ)));

        shapeAttributes.addTab("General", new JScrollPane(attributes));

        JPanel textures = new JPanel(new BorderLayout());
        JPanel openTexture = new JPanel(new BorderLayout());
        JTextField texturePath = new JTextField();
        texturePath.setEditable(false);
        openTexture.add(texturePath, BorderLayout.CENTER);
        JButton open = new JButton("...");
        open.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(AMCMEditor.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            chooser.setFileFilter(new FileNameExtensionFilter("Model Texture (.png)", "png"));
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    currentTexture = ImageIO.read(chooser.getSelectedFile());
                    texturePath.setText(chooser.getSelectedFile().getAbsolutePath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                textures.paintAll(textures.getGraphics());
            }
        });
        openTexture.add(open, BorderLayout.EAST);
        textures.add(openTexture, BorderLayout.NORTH);

        JPanel textureOuter = new JPanel(new BorderLayout());
        JPanel texturepanel = new TexturedPanel();
        texturepanel.setPreferredSize(new Dimension(512, 512));
        textureOuter.add(texturepanel, BorderLayout.CENTER);
        textureOuter.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Texture map:"));
        textures.add(textureOuter, BorderLayout.CENTER);

        JPanel textureAttributes = new JPanel();
        textureAttributes.setLayout(new BoxLayout(textureAttributes, BoxLayout.Y_AXIS));
        JPanel textureFacingOuter = new JPanel(new BorderLayout());
        JPanel textureFacingInner = new JPanel(new BorderLayout());
        textureFacingInner.add(facingLabel, BorderLayout.CENTER);
        textureFacingInner.add(new FaceChanger(EnumFacing.NORTH, texturepanel), BorderLayout.NORTH);
        textureFacingInner.add(new FaceChanger(EnumFacing.SOUTH, texturepanel), BorderLayout.SOUTH);
        textureFacingInner.add(new FaceChanger(EnumFacing.WEST, texturepanel), BorderLayout.WEST);
        textureFacingInner.add(new FaceChanger(EnumFacing.EAST, texturepanel), BorderLayout.EAST);
        textureFacingOuter.add(new FaceChanger(EnumFacing.UP, texturepanel), BorderLayout.NORTH);
        textureFacingOuter.add(textureFacingInner, BorderLayout.CENTER);
        textureFacingOuter.add(new FaceChanger(EnumFacing.DOWN, texturepanel), BorderLayout.SOUTH);
        textureAttributes.add(wrapWithNamedBorder("Texture facing:", textureFacingOuter));

        textures.add(textureAttributes, BorderLayout.EAST);

        JCheckBox customTextureFlag = new JCheckBox("Set custom texture for shape");
        JButton writeTexture = new JButton("Write to shape");
        textures.add(getCentered(customTextureFlag, writeTexture), BorderLayout.AFTER_LAST_LINE);

        shapeAttributes.addTab("Textures", textures);

        frame.add(shapeAttributes, BorderLayout.CENTER);
    }

    private static JPanel getWithLabel(String label, Component c){
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        JLabel jLabel = new JLabel(label);
        panel.add(jLabel);
        panel.add(c);
        panel.setPreferredSize(new Dimension(jLabel.getPreferredSize().width + c.getPreferredSize().width, Math.max(jLabel.getPreferredSize().height, c.getPreferredSize().height)));
        return panel;
    }

    private static JPanel wrapWithNamedBorder(String title, Component... cs){
        JPanel panel = new JPanel(new GridLayout(cs.length, 1));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true), title));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        for(Component c : cs){
            panel.add(c);
        }
        return panel;
    }

    public static JPanel getCentered(Component... cs){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for(Component c : cs){
            panel.add(c);
        }
        return panel;
    }

    private static void updateListModel(){
        shapes.setModel(new AbstractListModel<IAMCMShapes>() {
            @Override
            public int getSize() {
                return modelShapes.size();
            }
            @Override
            public IAMCMShapes getElementAt(int index) {
                return modelShapes.get(index);
            }
        });
    }

    private static JComboBox<String> shapeType = new JComboBox<>(new String[]{"Box", "Triangle"});
    private static JSpinner shapeWidth = new JSpinner(), shapeHeight = new JSpinner(), shapeDepth = new JSpinner();
    private static JSpinner shapeOffsetX = new JSpinner(), shapeOffsetY = new JSpinner(), shapeOffsetZ = new JSpinner();
    private static JSpinner shapeRotPointX = new JSpinner(), shapeRotPointY = new JSpinner(), shapeRotPointZ = new JSpinner();
    private static JSpinner shapeRotAngleX = new JSpinner(), shapeRotAngleY = new JSpinner(), shapeRotAngleZ = new JSpinner();

    private static void updateShapePanel(IAMCMShapes shape){
        if(shape instanceof AMCMDefaultShape){
            AMCMDefaultShape s = (AMCMDefaultShape) shape;
            if(shape instanceof AMCMBox){
                shapeType.setSelectedIndex(0);
            } else {
                shapeType.setSelectedIndex(1);
            }
            shapeWidth.setEnabled(true);
            shapeWidth.setModel(new SpinnerNumberModel(s.width, null, null, 1));
            shapeWidth.addChangeListener(e -> saveShape(s));
            shapeHeight.setEnabled(true);
            shapeHeight.setModel(new SpinnerNumberModel(s.height, null, null, 1));
            shapeHeight.addChangeListener(e -> saveShape(s));
            shapeDepth.setEnabled(true);
            shapeDepth.setModel(new SpinnerNumberModel(s.depth, null, null, 1));
            shapeDepth.addChangeListener(e -> saveShape(s));
            shapeOffsetX.setEnabled(true);
            shapeOffsetX.setModel(new SpinnerNumberModel(s.offset.x, null, null, 0.1F));
            shapeOffsetX.addChangeListener(e -> saveShape(s));
            shapeOffsetY.setEnabled(true);
            shapeOffsetY.setModel(new SpinnerNumberModel(s.offset.y, null, null, 0.1F));
            shapeOffsetY.addChangeListener(e -> saveShape(s));
            shapeOffsetZ.setEnabled(true);
            shapeOffsetZ.setModel(new SpinnerNumberModel(s.offset.z, null, null, 0.1F));
            shapeOffsetZ.addChangeListener(e -> saveShape(s));
            shapeRotPointX.setEnabled(true);
            shapeRotPointX.setModel(new SpinnerNumberModel(s.rotationPoint.x, null, null, 0.1F));
            shapeRotPointX.addChangeListener(e -> saveShape(s));
            shapeRotPointY.setEnabled(true);
            shapeRotPointY.setModel(new SpinnerNumberModel(s.rotationPoint.y, null, null, 0.1F));
            shapeRotPointY.addChangeListener(e -> saveShape(s));
            shapeRotPointZ.setEnabled(true);
            shapeRotPointZ.setModel(new SpinnerNumberModel(s.rotationPoint.z, null, null, 0.1F));
            shapeRotPointZ.addChangeListener(e -> saveShape(s));
            shapeRotAngleX.setEnabled(true);
            shapeRotAngleX.setModel(new SpinnerNumberModel(s.rotationAngle.x, null, null, 0.1F));
            shapeRotAngleX.addChangeListener(e -> saveShape(s));
            shapeRotAngleY.setEnabled(true);
            shapeRotAngleY.setModel(new SpinnerNumberModel(s.rotationAngle.y, null, null, 0.1F));
            shapeRotAngleY.addChangeListener(e -> saveShape(s));
            shapeRotAngleZ.setEnabled(true);
            shapeRotAngleZ.setModel(new SpinnerNumberModel(s.rotationAngle.z, null, null, 0.1F));
            shapeRotAngleZ.addChangeListener(e -> saveShape(s));
        }
    }

    private static void saveShape(IAMCMShapes shape){
        if(shape instanceof AMCMDefaultShape){
            ((AMCMDefaultShape) shape).setDimension((int)shapeWidth.getValue(), (int)shapeHeight.getValue(), (int)shapeDepth.getValue());
            ((AMCMDefaultShape) shape).setOffset((float)shapeOffsetX.getValue(), (float)shapeOffsetY.getValue(), (float)shapeOffsetZ.getValue());
            ((AMCMDefaultShape) shape).setRotationPoint((float)shapeRotPointX.getValue(), (float)shapeRotPointY.getValue(), (float)shapeRotPointZ.getValue());
            ((AMCMDefaultShape) shape).setRotateAngle((float)shapeRotAngleX.getValue(), (float)shapeRotAngleY.getValue(), (float)shapeRotAngleZ.getValue());
        }
    }

    private static void saveFile(JFrame frame, boolean openFileDialog){
        if(!modelShapes.isEmpty()){
            if(openFileDialog || saveFile == null){
                JFileChooser chooser = new JFileChooser(AMCMEditor.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                chooser.setFileFilter(new FileNameExtensionFilter("AMCModel (.amcm)", "amcm"));
                if(chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    saveFile = chooser.getSelectedFile();
                }
            }
            if(saveFile != null){
                try {
                    if(saveFile.exists()){
                        if(JOptionPane.showConfirmDialog(frame, "Do you want to override the old file?") != JOptionPane.YES_OPTION){
                            return;
                        }
                    }
                    AMCMWriter.writeToFile(saveFile, new AMCMPredefinedModel(modelShapes).setTexture(actualModel != null ? actualModel.getTexture() : null));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    static class FaceChanger extends JButton{
        public FaceChanger(EnumFacing facing, Component repaint){
            super(facing.getName());
            this.addActionListener(e -> {
                AMCMEditor.currenttextureFacing = facing;
                facingLabel.setText(facing.getName());
                repaint.paintAll(repaint.getGraphics());
            });
        }
    }

    static class TexturedPanel extends JPanel{
        private double scale = 1.0D;
        private int offsetX = 0, offsetY = 0;
        private Point oldPoint = null;

        public TexturedPanel(){
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1){
                        scale += 0.2D;
                    } else if(e.getButton() == MouseEvent.BUTTON3){
                        scale -= 0.2D;
                    }
                    TexturedPanel.this.paintComponent(TexturedPanel.this.getGraphics());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    oldPoint = null;
                }
            });
            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (oldPoint == null) {
                        oldPoint = e.getLocationOnScreen();
                    } else {
                        offsetX -= ((oldPoint.getX() - e.getLocationOnScreen().getX()));
                        offsetY -= ((oldPoint.getY() - e.getLocationOnScreen().getY()));
                        oldPoint = e.getLocationOnScreen();
                    }
                    TexturedPanel.this.paintComponent(TexturedPanel.this.getGraphics());
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(currentTexture != null){
                g.translate(offsetX, offsetY);
                ((Graphics2D)g).scale(scale, scale);
                g.drawImage(currentTexture, 0, 0, null);
                IAMCMShapes shape = shapes.getSelectedValue();
                if(shape instanceof AMCMDefaultShape){
                    int[] off = (int[]) ((AMCMDefaultShape) shape).textureOffsets.getOrDefault(currenttextureFacing, new int[]{0, 0});
                    g.setColor(Color.white);
                    int w = ((AMCMDefaultShape) shape).width;
                    int h = ((AMCMDefaultShape) shape).height;
                    switch (currenttextureFacing){
                        case EAST: case WEST: {
                            w = ((AMCMDefaultShape) shape).depth;
                            break;
                        }
                        case DOWN: case UP: {
                            h = ((AMCMDefaultShape) shape).depth;
                            break;
                        }
                    }
                    g.drawRect(off[0] - 1, off[1] - 1, w + 2, h + 1);
                }
            }
        }
    }

}
