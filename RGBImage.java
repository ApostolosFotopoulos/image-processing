package ce325.hw2;

public class RGBImage implements Image {
    RGBPixel[][] pixelbuf;
    int width;
    int height;
    int colordepth;
    static final int MAX_COLORDEPTH = 255;
    
    /*Kataskeuasths pou dhmiourgei mia RGBImage kai arxikopoiei ola ta pixels
    me red=0,green=0,blue=0.Dhmiourgei ousiastika mia maurh eikona.*/
    public RGBImage(int width, int height, int colordepth) {
        this.width = width;
        this.height = height; 
        this.colordepth = colordepth;
        
        pixelbuf = new RGBPixel[height][width];
        
        for(int i=0; i<height; i++) {
            for(int k=0; k<width; k++) {
                pixelbuf[i][k] = new RGBPixel((short)0,(short)0,(short)0);
            }
        }          
    }
    
    //Kataskeuasths pou antigraphei mia RGBImage.
    public RGBImage(RGBImage copyImg) {
        width = copyImg.width;
        height = copyImg.height;
        colordepth = copyImg.colordepth;
        
        pixelbuf = new RGBPixel[copyImg.height][copyImg.width];
        
        for(int i=0; i<copyImg.height; i++) {
            for(int k=0; k<copyImg.width; k++) {
                pixelbuf[i][k] = new RGBPixel(copyImg.pixelbuf[i][k]);
            }
        }
    }
    
    /*Kataskeuasths mias RGBImage apo mia YUVImage.
    Ousiastika allazei h kwdikopoihsh twn pixel apo YUV se RGB.*/
    public RGBImage(YUVImage YUVImg) {
        width = YUVImg.width;
        height = YUVImg.height;
        colordepth = MAX_COLORDEPTH;
        
        pixelbuf = new RGBPixel[YUVImg.height][YUVImg.width];
        
        for(int i=0; i<YUVImg.height; i++) {
            for(int k=0; k<YUVImg.width; k++) {
                pixelbuf[i][k] = new RGBPixel(YUVImg.pixelbuf[i][k]);
            }
        }
    }
    
    //Methodos gia thn metatroph mias RGB eikonas se aspromaurh.
    public void grayscale() {
        short gray, red, green, blue;

        for (int i=0; i<height; i++) {
            for (int k=0; k<width; k++) {
                red = pixelbuf[i][k].getRed();
                green = pixelbuf[i][k].getGreen();
                blue = pixelbuf[i][k].getBlue();

                gray = (short)(red * 0.3 + green * 0.59 + blue * 0.11);
                
                if (gray > colordepth) { 
                    gray = (short) colordepth; 
                }

                pixelbuf[i][k].setRed(gray);
                pixelbuf[i][k].setGreen(gray);
                pixelbuf[i][k].setBlue(gray);
            }
        }
    }
    
    //Methodos gia to diplasiasmo tou megethous mias RGB eikonas.
    public void doublesize() {
        short red,green,blue;
        
        RGBImage newImg = new RGBImage(2*width ,2*height ,colordepth);
        
        for (int i=0; i< height; i++) {
            for (int k=0; k<width; k++) {
                 red = pixelbuf[i][k].getRed();
                 green = pixelbuf[i][k].getGreen();
                 blue = pixelbuf[i][k].getBlue();
                 
                 newImg.pixelbuf[2*i][2*k].setRed(red);
                 newImg.pixelbuf[2*i][2*k].setGreen(green);
                 newImg.pixelbuf[2*i][2*k].setBlue(blue);
                 
                 newImg.pixelbuf[2*i+1][2*k].setRed(red);
                 newImg.pixelbuf[2*i+1][2*k].setGreen(green);
                 newImg.pixelbuf[2*i+1][2*k].setBlue(blue);
                 
                 newImg.pixelbuf[2*i][2*k+1].setRed(red);
                 newImg.pixelbuf[2*i][2*k+1].setGreen(green);
                 newImg.pixelbuf[2*i][2*k+1].setBlue(blue);
                 
                 newImg.pixelbuf[2*i+1][2*k+1].setRed(red);
                 newImg.pixelbuf[2*i+1][2*k+1].setGreen(green);
                 newImg.pixelbuf[2*i+1][2*k+1].setBlue(blue);
            }
        }
        
        height = newImg.height;
        width = newImg.width;
        colordepth = newImg.colordepth;
        pixelbuf = newImg.pixelbuf;
    }
    
    //Methodos gia ton ypodiplasiasmo tou megethous mias RGB eikonas.
    public void halfsize() {
        short red,green,blue;
        
        RGBImage newImg = new RGBImage(width/2 ,height/2 ,colordepth);
        
        for (int i=0; i<height/2; i++) {
            for (int k=0; k < width/2; k++) {
                
                red = (short) ((pixelbuf[2*i][2*k].getRed() + pixelbuf[2*i+1][2*k].getRed() + pixelbuf[2*i][2*k+1].getRed() + pixelbuf[2*i+1][2*k+1].getRed())/4);
                green = (short) ((pixelbuf[2*i][2*k].getGreen() + pixelbuf[2*i+1][2*k].getGreen() +pixelbuf[2*i][2*k+1].getGreen() + pixelbuf[2*i+1][2*k+1].getGreen())/4);
                blue = (short) ((pixelbuf[2*i][2*k].getBlue() + pixelbuf[2*i+1][2*k].getBlue() + pixelbuf[2*i][2*k+1].getBlue() +pixelbuf[2*i+1][2*k+1].getBlue())/4);
                 
                newImg.pixelbuf[i][k].setRed(red);
                newImg.pixelbuf[i][k].setGreen(green);
                newImg.pixelbuf[i][k].setBlue(blue);
                 
            }
        }
        
        height = newImg.height;
        width = newImg.width;
        colordepth = newImg.colordepth;
        pixelbuf = newImg.pixelbuf;
    }
    
    //Methodos gia thn dexiostrofh peristrofh mias RGB eikonas kata 90 moires.
    public void rotateClockwise() {
        RGBPixel[][] newPixelbuf = new RGBPixel[width][height];
        
        for(int i=0; i<height; i++) {
            for(int k=0; k<width; k++) {
                newPixelbuf[k][height-i-1] = pixelbuf[i][k];
            }
        }
        
        int temp = width;
        width = height;
        height = temp;
        pixelbuf = newPixelbuf;
    }
    
    //Methodos gia thn aristerostrofh peristrofh mias RGB eikonas kata 90 moires.
    public void rotateCounterClockwise() {
        RGBPixel[][] newPixelbuf = new RGBPixel[width][height];
        
        for(int i=0; i<height; i++) {
            for(int k=0; k<width; k++) {
                newPixelbuf[width-k-1][i] = pixelbuf[i][k];
            }
        }
        
        int temp = width;
        width = height;
        height = temp;
        pixelbuf = newPixelbuf;
    }  
}
