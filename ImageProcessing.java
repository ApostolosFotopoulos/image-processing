package ce325.hw2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public class ImageProcessing {
    boolean imageExists; //Elegkths gia to an yparxei kapoia anoxth eikona.
    JFrame appWindow;
    JPanel statusPanel;
    JLabel status;
    YUVImage yuvImage; //An h eikona einai typou YUV apothikeuetai edw.
    PPMImage ppmImage; //An h eikona einai typou PPM apothikeuetai edw.
    String imageType; //O typos ths eikonas pou exoume anoixei.
    
    //Xrhsimopoiountai gia to reset ths eikonas.
    YUVImage originalYUVImage; //H arxikh eikona typou YUV pou anoixe o xrhsths.
    PPMImage originalPPMImage; //H arxikh eikona typou PPM pou anoixe o xrhsths.
    
    //Xrhsimopoiountai gia to parathyro pou emfanizetai se periptwsh pou to arxeio pou epilexei o xrhsths yparxei hdh.
    File overwriteSaveFile; //To arxeio sto opoio tha apothikeutei h eikona an o xrhsths epilexei na kanei overwrite.
    File newSaveFile; //To arxeio sto opoio tha apothikeutei h eikona an o xrhsths epilexei na kanei apothikeusei thn eikona se neo arxeio.
    
    public ImageProcessing() {
        appWindow = new JFrame("~~~Image Processing~~~");
        appWindow.setSize(500,500);
        appWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appWindow.setLocationRelativeTo(null);
        appWindow.setResizable(false);
        appWindow.setVisible(true);
        
        statusPanel = new JPanel();  //statusPanel = new JPanel(new GridLayout(0,1)); 
        appWindow.add(statusPanel);
        status = new JLabel("No image loaded");
        statusPanel.add(status , BorderLayout.NORTH);
        imageExists = false;
        
        JMenuBar menu = new JMenuBar();
        appWindow.setJMenuBar(menu);
        
        //////////////////////////////////////FILE//////////////////////////////////////////////////
        
        JMenu fileMenu = new JMenu("File");
        menu.add(fileMenu);
        
        JMenu openImg = new JMenu("Open");
        fileMenu.add(openImg);
        
        //Anoigma enos PPM arxeiou kai probolh tou sto Swing.
        JMenuItem openPPMFile = new JMenuItem(new AbstractAction("PPM File"){
            public void actionPerformed (ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                
                if (fc.showOpenDialog(appWindow) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    imageType = "PPM";
                    
                    ppmImage = new PPMImage(file);
                    originalPPMImage = new PPMImage(ppmImage);
                    if (ppmImage.height!=0 && ppmImage.width!=0) {
                        DisplayImage();
                    }  
                }
            }
        });
        openImg.add(openPPMFile);
        
        //Anoigma enos YUV arxeiou kai probolh tou sto Swing.
        JMenuItem openYUVFile = new JMenuItem(new AbstractAction("YUV File"){
            public void actionPerformed (ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                
                if (fc.showOpenDialog(appWindow) == JFileChooser.APPROVE_OPTION) {
                    File openFile = fc.getSelectedFile();
                    imageType = "YUV";
                    
                    yuvImage = new YUVImage(openFile);
                    originalYUVImage = new YUVImage(yuvImage);
                    if (yuvImage.height!=0 && yuvImage.width!=0) {
                        DisplayImage();
                    }
                }
            }
        });
        openImg.add(openYUVFile);
        
        //Anoigma enos JPG arxeiou kai probolh tou sto Swing.
        JMenuItem openJPGFile = new JMenuItem(new AbstractAction("JPG File"){
            public void actionPerformed (ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                
                if (fc.showOpenDialog(appWindow) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    imageType = "PPM";
                    
                    ppmImage = new PPMImage(file);
                    originalPPMImage = new PPMImage(ppmImage);
                    if (ppmImage.height!=0 && ppmImage.width!=0) {
                        DisplayImage();
                    }  
                }
            }
        });
        openImg.add(openJPGFile);
        
        JMenu saveImg = new JMenu("Save As");
        fileMenu.add(saveImg);
        
        /*Apothikeush ths eikonas pou exoume anoixei sto Swing ws ena arxio typou PPM.
        An o xrhsths dwsei onoma me lathos katalhxh h onoma xwris katalhxh tote auth
        metatrepetai se .ppm*/
        JMenuItem savePPMFile = new JMenuItem(new AbstractAction("PPM File"){     
            public void actionPerformed (ActionEvent e) {
                if (imageExists) {  
                    JFileChooser fc = new JFileChooser();
                    fc.setCurrentDirectory(new File("."));
                    
                    String filename = "";
                    File saveFile = new File(filename);
                    if (fc.showSaveDialog(appWindow) == JFileChooser.APPROVE_OPTION) {
                        saveFile = fc.getSelectedFile();

                        filename = saveFile.getName();

                        //Elegxos gia swsth onomasia arxeiou.
                        if (!filename.endsWith(".ppm")) {
                            //An exei diaforetikh katalhxh tote auth allazei se .ppm.
                            if (filename.matches(".*\\..*")) {
                                int dotPos = filename.indexOf(".");
                                filename = filename.substring(0, dotPos);
                                filename = filename + ".ppm";
                            }
                            //An den exei katalhxh tote prostithetai h katalhxh .ppm
                            else {
                                filename = filename + ".ppm";
                            }
                        }
                        
                        saveFile = new File(saveFile.getParentFile() ,filename);
                        
                        //An to arxeio pou epilexei o xrhsths yparxei hdh tote emfanizei ena parathyro me dyo epiloges.
                        if (saveFile.exists()) {
                            //Dhmiourgei ena neo JDialog kai empodizei thn prosbash sto kyriws parathyro.
                            JDialog fileExistsFrame = new JDialog(appWindow,"~~~Image Processing~~~",Dialog.ModalityType.APPLICATION_MODAL);
                            fileExistsFrame.setSize(450,210);
                            fileExistsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            fileExistsFrame.setLocationRelativeTo(null);
                            fileExistsFrame.setLayout(null);
                            fileExistsFrame.getContentPane().setBackground(Color.lightGray); //Allazei to background tou parathyrou.
                            fileExistsFrame.setResizable(false); //To parathyro den allazei diastaseis.
                           
                            JLabel msg = new JLabel("<html>The file " + saveFile.getName() + " already exists<br/>Do you want to:<html>");
                            msg.setBounds(10, 5, 410, 40);
                            fileExistsFrame.add(msg);
                            
                            overwriteSaveFile = new File(saveFile.getParentFile() ,filename);
                            
                            //Koumpi pou dinei th dynatothta sto xrhsth na antikatasthsei to arxeio.
                            JButton overwrite = new JButton(new AbstractAction("Replace existing file."){ 
                                public void actionPerformed (ActionEvent e) {
                                    if (imageType.equals("PPM")) {
                                        ppmImage.toFile(overwriteSaveFile);
                                    }
                                    else { 
                                        PPMImage YUVtoPPM = new PPMImage(yuvImage);
                                        YUVtoPPM.toFile(overwriteSaveFile); 
                                    }
                                    fileExistsFrame.dispose();
                                }
                            });
                            overwrite.setBounds(10, 75, 410, 40);
                            overwrite.setBackground(Color.BLACK);
                            overwrite.setForeground(Color.GRAY);
                            fileExistsFrame.add(overwrite);
                            
                            //Afairei prosorina to extension gia thn tropopoihsh tou onomatos.
                            int dotPos = filename.indexOf(".");
                            filename = filename.substring(0, dotPos);
                            
                            int newCopyNumber = 1;
                            String extension = "(" + newCopyNumber + ")" + ".ppm" ;
                            String newFilename = filename + extension;
                            saveFile = new File(saveFile.getParentFile() ,newFilename);
                            
                            //Elegxei an yparxoun hdh alla antigrafa.
                            while (saveFile.exists()) { 
                                extension = "(" + newCopyNumber++ + ")" + ".ppm" ;
                                newFilename = filename + extension;
                                saveFile = new File(saveFile.getParentFile() ,newFilename);
                            }      
                            
                            newSaveFile = new File(saveFile.getParentFile() ,newFilename);
                            
                            //Koumpi pou dinei th dynatothta sto xrhsth na dhmiourghsei ena neo arxeio me paromoia onomasia.
                            JButton createNewFile = new JButton(new AbstractAction("Create a new file named " + newFilename){  
                                public void actionPerformed (ActionEvent e) {
                                    if (imageType.equals("PPM")) {
                                        ppmImage.toFile(newSaveFile);
                                    }
                                    else { 
                                        PPMImage YUVtoPPM = new PPMImage(yuvImage);
                                        YUVtoPPM.toFile(newSaveFile); 
                                    }
                                    fileExistsFrame.dispose();
                                }
                            });
                            createNewFile.setBounds(10, 125, 410, 40);
                            createNewFile.setBackground(Color.BLACK);
                            createNewFile.setForeground(Color.GRAY);
                            fileExistsFrame.add(createNewFile);
                            fileExistsFrame.setVisible(true);
                        }
                        //An to arxeio den yparxei.
                        else {
                            if (imageType.equals("PPM")) {
                                ppmImage.toFile(saveFile);
                            }
                            else { 
                                PPMImage YUVtoPPM = new PPMImage(yuvImage);
                                YUVtoPPM.toFile(saveFile); 
                            }    
                        }      
                    }
                }
            }
        });
        saveImg.add(savePPMFile);
        
        /*Apothikeush ths eikonas pou exoume anoixei sto Swing ws ena arxio typou YUV.
        An o xrhsths dwsei onoma me lathos katalhxh h onoma xwris katalhxh tote auth
        metatrepetai se .yuv*/
        JMenuItem saveYUVFile = new JMenuItem(new AbstractAction("YUV File"){
            public void actionPerformed (ActionEvent e) {
                if (imageExists) {  
                    JFileChooser fc = new JFileChooser();
                    fc.setCurrentDirectory(new File("."));
                    
                    String filename = "";
                    File saveFile = new File(filename);
                    if (fc.showSaveDialog(appWindow) == JFileChooser.APPROVE_OPTION) {
                        saveFile = fc.getSelectedFile();

                        filename = saveFile.getName();
                        //Elegxos gia swsth onomasia arxeiou.
                        if (!filename.endsWith(".yuv")) {
                            //An exei diaforetikh katalhxh tote auth allazei se .yuv.
                            if (filename.matches(".*\\..*")) {
                                int dotPos = filename.indexOf(".");
                                filename = filename.substring(0, dotPos);
                                filename = filename + ".yuv";
                            }
                            //An den exei katalhxh tote prostithetai h katalhxh .yuv.
                            else {
                                filename = filename + ".yuv";
                            }
                        }
                        
                        saveFile = new File(saveFile.getParentFile() ,filename);
                        
                        //An to arxeio pou epilexei o xrhsths yparxei hdh tote emfanizei ena parathyro me dyo epiloges.
                        if (saveFile.exists()) {
                            //Dhmiourgei ena neo JDialog kai empodizei thn prosbash sto kyriws parathyro.
                            JDialog fileExistsFrame = new JDialog(appWindow,"~~~Image Processing~~~",Dialog.ModalityType.APPLICATION_MODAL);
                            fileExistsFrame.setSize(450,210);
                            fileExistsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            fileExistsFrame.setLocationRelativeTo(null);
                            fileExistsFrame.setLayout(null);
                            fileExistsFrame.getContentPane().setBackground(Color.lightGray); //Allazei to background tou parathyrou.
                            fileExistsFrame.setResizable(false); //To parathyro den allazei diastaseis.
                           
                            JLabel msg = new JLabel("<html>The file " + saveFile.getName() + " already exists<br/>Do you want to:<html>");
                            msg.setBounds(10, 5, 410, 40);
                            fileExistsFrame.add(msg);
                            
                            overwriteSaveFile = new File(saveFile.getParentFile() ,filename);
                            
                            //Koumpi pou dinei th dynatothta sto xrhsth na antikatasthsei to arxeio.
                            JButton overwrite = new JButton(new AbstractAction("Replace existing file."){ 
                                public void actionPerformed (ActionEvent e) {
                                    if (imageType.equals("YUV")) {
                                        yuvImage.toFile(overwriteSaveFile);
                                    }
                                    else { 
                                        YUVImage PPMtoYUV = new YUVImage(ppmImage);
                                        PPMtoYUV.toFile(overwriteSaveFile);
                                    }
                                    fileExistsFrame.dispose();
                                }
                            });
                            overwrite.setBounds(10, 75, 410, 40);
                            overwrite.setBackground(Color.BLACK);
                            overwrite.setForeground(Color.GRAY);
                            fileExistsFrame.add(overwrite);
                            
                            //Afairei prosorina to extension gia thn tropopoihsh tou onomatos.
                            int dotPos = filename.indexOf(".");
                            filename = filename.substring(0, dotPos);
                            
                            int newCopyNumber = 1;
                            String extension = "(" + newCopyNumber + ")" + ".yuv" ;
                            String newFilename = filename + extension;
                            saveFile = new File(saveFile.getParentFile() ,newFilename);
                            
                            //Elegxei an yparxoun hdh alla antigrafa.
                            while (saveFile.exists()) { 
                                extension = "(" + newCopyNumber++ + ")" + ".yuv" ;
                                newFilename = filename + extension;
                                saveFile = new File(saveFile.getParentFile() ,newFilename);
                            }      
                            
                            newSaveFile = new File(saveFile.getParentFile() ,newFilename);
                            
                            //Koumpi pou dinei th dynatothta sto xrhsth na dhmiourghsei ena neo arxeio me paromoia onomasia.
                            JButton createNewFile = new JButton(new AbstractAction("Create a new file named " + newFilename){  
                                public void actionPerformed (ActionEvent e) {
                                    if (imageType.equals("YUV")) {
                                        yuvImage.toFile(newSaveFile);
                                    }
                                    else { 
                                        YUVImage PPMtoYUV = new YUVImage(ppmImage);
                                        PPMtoYUV.toFile(newSaveFile);
                                    }
                                    fileExistsFrame.dispose();
                                }
                            });
                            createNewFile.setBounds(10, 125, 410, 40);
                            createNewFile.setBackground(Color.BLACK);
                            createNewFile.setForeground(Color.GRAY);
                            fileExistsFrame.add(createNewFile);
                            fileExistsFrame.setVisible(true);
                        }
                        //An to arxeio den yparxei.
                        else {
                            if (imageType.equals("YUV")) {
                                yuvImage.toFile(saveFile);
                            }
                            else { 
                                YUVImage PPMtoYUV = new YUVImage(ppmImage);
                                PPMtoYUV.toFile(saveFile);
                            }    
                        }
                    }
                }
            }
        });
        saveImg.add(saveYUVFile);
        
        //////////////////////////////////////ACTIONS//////////////////////////////////////////////////
        
        JMenu actionsMenu = new JMenu("Actions");
        menu.add(actionsMenu);
        
        //Metatrepei thn eikona pou exoume anoixei se aspromaurh.
        JMenuItem grayscale = new JMenuItem(new AbstractAction("Grayscale"){
            public void actionPerformed (ActionEvent e) {
                if (imageExists) {
                    if (imageType.equals("PPM")) {
                        ppmImage.grayscale();
                    }
                    else {
                        yuvImage.grayscale();
                    }
                    DisplayImage();
                } 
            }
        });
        actionsMenu.add(grayscale);
        
        //Diplasiazei to megethos ths eikonas.
        JMenuItem increaseSize = new JMenuItem(new AbstractAction("Increase Size"){
            public void actionPerformed (ActionEvent e) {
               if (imageExists) {
                    if (imageType.equals("PPM")) {
                        ppmImage.doublesize();
                    }
                    else {
                        yuvImage.doublesize();
                    }
                    DisplayImage();
                }  
            }
        });
        actionsMenu.add(increaseSize);
        
        //Ypodiplasiazei to megethos ths eikonas.
        JMenuItem decreaseSize = new JMenuItem(new AbstractAction("Decrease Size"){
            public void actionPerformed (ActionEvent e) {
                if (imageExists) {
                    if (imageType.equals("PPM")) {
                        ppmImage.halfsize();
                    }
                    else {
                        yuvImage.halfsize();
                    }
                    DisplayImage();
                } 
            }
        });
        actionsMenu.add(decreaseSize);
        
        //Peristrefei dexiostrofa thn eikona.
        JMenuItem rotateClockwise = new JMenuItem(new AbstractAction("Rotate Clockwise"){
            public void actionPerformed (ActionEvent e) {
                if (imageExists) {
                    if (imageType.equals("PPM")) {
                        ppmImage.rotateClockwise();
                    }
                    else {
                        yuvImage.rotateClockwise();
                    }
                    DisplayImage();
                }
            }
        });
        actionsMenu.add(rotateClockwise);
        
        //Peristrefei aristerostrofa thn eikona.
        JMenuItem rotateCounterClockwise = new JMenuItem(new AbstractAction("Rotate Counter Clockwise"){
            public void actionPerformed (ActionEvent e) {
                if (imageExists) {
                    if (imageType.equals("PPM")) {
                        ppmImage.rotateCounterClockwise();
                    }
                    else {
                        yuvImage.rotateCounterClockwise();
                    }
                    DisplayImage();
                }
            }
        });
        actionsMenu.add(rotateCounterClockwise);
        
        /*Dhmiourgei to istogramma ths eikonas , to exisisorropei kai sth synexeia probalei
        thn eikona pou prokyptei apo tis nees times fwteinothtas pou prokyptoun.Parallhla paragei
        ena arxeio typou txt pou periexei tosous xarakthres "*" osous antistoixoun ston arithmo twn 
        pixel pou exoun th sygkekrymenh fwteinothta(analogika).*/
        JMenuItem equalizeHistogram = new JMenuItem(new AbstractAction("Equalize Histogram"){
            public void actionPerformed (ActionEvent e) {
                if (imageExists) {
                    if (imageType.equals("PPM")) {
                        YUVImage PPMtoYUV = new YUVImage(ppmImage);
                        Histogram histogram = new Histogram(PPMtoYUV);
                        histogram.toFile(new File("Histogram.txt"));
                        histogram.equalize();
                        PPMImage YUVtoPPM = new PPMImage(PPMtoYUV);
                        ppmImage = YUVtoPPM;
                    }
                    else {
                        Histogram histogram = new Histogram(yuvImage);
                        histogram.toFile(new File("Histogram.txt"));
                        histogram.equalize();
                    }
                    DisplayImage();
                }
            }  
        });
        actionsMenu.add(equalizeHistogram);
        
        JMenu stackingAlgorithm = new JMenu("Stacking Algorithm");
        actionsMenu.add(stackingAlgorithm);
        
        //Efarmozei thn diadikasia stacking gia enan arithmo eikonwn tis opoies exei epilexei o xrhsths.
        JMenuItem multipleFiles = new JMenuItem(new AbstractAction("Select multiple files"){
            public void actionPerformed (ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                fc.setMultiSelectionEnabled(true);
                
                if (fc.showOpenDialog(appWindow) == JFileChooser.APPROVE_OPTION) {
                    File[] files = fc.getSelectedFiles();
                    
                     //Dhmiourgia enos parathyrou pou paramenei anoixto oso pragmatopoieitai h diadikasia stacking.
                    JDialog waitForStacker = new JDialog(appWindow,"~~~Image Processing~~~",Dialog.ModalityType.APPLICATION_MODAL);
                    waitForStacker.setSize(300,150);
                    waitForStacker.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    waitForStacker.setLocationRelativeTo(null);
                    waitForStacker.setUndecorated(true); //Afairei to exwteriko tou parathyrou.
                    waitForStacker.getContentPane().setBackground(Color.DARK_GRAY); //Allazei to background tou parathyrou.
                    waitForStacker.setResizable(false); //To parathyro den allazei diastaseis.
                    
                    JLabel msg = new JLabel("<html>Processing...<br/><br/>Please wait...<html>");
                    msg.setForeground(Color.white); //Allazei to xrwma tou text se aspro.
                    waitForStacker.add(msg, BorderLayout.CENTER);
                    msg.setHorizontalAlignment(SwingConstants.CENTER); //Metaferei to mynhma sto kentro tou parathyrou.
                    
                    SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>() {
                        protected Void doInBackground() {
                            PPMImageStacker stacker = new PPMImageStacker(files);
                            stacker.stack();
                            ppmImage = stacker.getStackedImage();
                            return null;
                        }
                        
                        protected void done() {
                            waitForStacker.dispose();
                        }
                    };
                    worker.execute();
                    waitForStacker.setVisible(true);
                                        
                    imageType = "PPM";
                    originalPPMImage = new PPMImage(ppmImage);
                    if (ppmImage.height!=0 && ppmImage.width!=0) {
                        DisplayImage();
                    } 
                }
            }
        });
        stackingAlgorithm.add(multipleFiles);
        
        //Efarmozei thn diadikasia stacking gia ena fakelo pou periexei polles eikones me thorybo.
        JMenuItem directory = new JMenuItem(new AbstractAction("Select directory"){
            public void actionPerformed (ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                if (fc.showOpenDialog(appWindow) == JFileChooser.APPROVE_OPTION) {
                    File dir = fc.getSelectedFile();
                    
                    //Dhmiourgia enos parathyrou pou paramenei anoixto oso pragmatopoieitai h diadikasia stacking.
                    JDialog waitForStacker = new JDialog(appWindow,"~~~Image Processing~~~",Dialog.ModalityType.APPLICATION_MODAL);
                    waitForStacker.setSize(300,150);
                    waitForStacker.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    waitForStacker.setLocationRelativeTo(null);
                    waitForStacker.setUndecorated(true); //Afairei to exwteriko tou parathyrou.
                    waitForStacker.getContentPane().setBackground(Color.DARK_GRAY); //Allazei to background tou parathyrou.
                    waitForStacker.setResizable(false); //To parathyro den allazei diastaseis.
                    
                    JLabel msg = new JLabel("<html>Processing...<br/><br/>Please wait...<html>");
                    msg.setForeground(Color.white); //Allazei to xrwma tou text se aspro.
                    waitForStacker.add(msg, BorderLayout.CENTER);
                    msg.setHorizontalAlignment(SwingConstants.CENTER); //Metaferei to mynhma sto kentro tou parathyrou.
                    
                    SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>() {
                        protected Void doInBackground() {
                            PPMImageStacker stacker = new PPMImageStacker(dir);
                            stacker.stack();
                            ppmImage = stacker.getStackedImage();
                            return null;
                        }
                        
                        protected void done() {
                            waitForStacker.dispose();
                        }
                    };
                    worker.execute();
                    waitForStacker.setVisible(true);
                    
                    imageType = "PPM";
                    originalPPMImage = new PPMImage(ppmImage);
                    if (ppmImage.height!=0 && ppmImage.width!=0) {
                        DisplayImage();
                    } 
                } 
            }
        });
        stackingAlgorithm.add(directory);
        
        //////////////////////////////////////TOOLS//////////////////////////////////////////////////
        
        JMenu toolsMenu = new JMenu("Tools");
        menu.add(toolsMenu);
        
        //Epanaferei thn arxikh eikona pou anoixe o xrhsths dixws tis tropopoihseis.
        JMenuItem resetImage = new JMenuItem(new AbstractAction("Reset Image"){
            public void actionPerformed (ActionEvent e) {
                if (imageExists) {
                    if (imageType.equals("PPM")) {
                        ppmImage = originalPPMImage;
                        originalPPMImage = new PPMImage(ppmImage);
                    }
                    else {
                        yuvImage = originalYUVImage;
                        originalYUVImage = new YUVImage(yuvImage);
                    }
                    DisplayImage();
                }
            }
        });
        toolsMenu.add(resetImage);
    }
    
    //Probalei thn eikona sto Swing.
    public void DisplayImage() {
        BufferedImage image = toBufferedImage();

        ImageIcon imgIcon = new ImageIcon(image);

        status = new JLabel();
        status.setIcon(imgIcon);
        statusPanel.removeAll();
        statusPanel.add(status , BorderLayout.CENTER);
        appWindow.pack();
        appWindow.setLocationRelativeTo(null);
        imageExists = true;
    }
    
    /*Metatrepei thn eikona pou exei anoixei o xrhsths se mia BufferedImage
    me skopo thn probolh ths sto Swing.*/
    public BufferedImage toBufferedImage() {
        BufferedImage image = null;
        if (imageType.equals("PPM")) {
            image = new BufferedImage(ppmImage.width, ppmImage.height, BufferedImage.TYPE_INT_RGB);
            
            for(int i=0; i<ppmImage.height; i++) {
                for(int k=0; k<ppmImage.width; k++) {
                    image.setRGB(k,i,ppmImage.pixelbuf[i][k].getPixel());
                }
            } 
        }
        else {
            image = new BufferedImage(yuvImage.width, yuvImage.height, BufferedImage.TYPE_INT_RGB);
            PPMImage YUVtoPPM = new PPMImage(yuvImage);
            
            for(int i=0; i<YUVtoPPM.height; i++) {
                for(int k=0; k<YUVtoPPM.width; k++) {
                    image.setRGB(k,i,YUVtoPPM.pixelbuf[i][k].getPixel());
                }
            }
        }
        
        return image;
    }
}
