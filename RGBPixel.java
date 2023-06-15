package ce325.hw2;

public class RGBPixel {
    private int pixel;
    
    //Kataskeuasths pou dhmiourgei ena pixel me tis times rgb.
    public RGBPixel(short red, short green, short blue) {
        pixel = (red<<16) | (green<<8) | (blue);
    }
    
    //Kataskeuasths pou dhmiourgei ena antigrapho enos pixel.
    public RGBPixel(RGBPixel pixel) {
        this.pixel = (pixel.getRed()<<16) | (pixel.getGreen()<<8) | (pixel.getBlue());
    }
    
    //Kataskeuasths pou metatrepei ena YUV pixel se kwdikopoihsh RGB.
    public RGBPixel(YUVPixel pixel) {
        int C = (pixel.getY() - 16);
        int D = (pixel.getU() - 128);
        int E = (pixel.getV() - 128);
        short red = clip(( 298 * C + 409 * E + 128) >> 8);
        short green = clip(( 298 * C - 100 * D - 208 * E + 128) >> 8);
        short blue = clip(( 298 * C + 516 * D + 128) >> 8);
        this.pixel = (red<<16)| (green<<8) | (blue);
    }
    
    /*Synarthsh pou epistrefei 255 an h timh ths eisodou einai megalyterh
    tou 255 , enw epistrefei 0 an h timh ths eisodou einai mikroterh tou 0.*/
    public static short clip(int input) {
        short returnVal = (short) input;
        if (input<0) {
            returnVal = 0;
        }
        if (input>255) {
            returnVal = 255;
        }
        
        return returnVal;
    }
    
    //Xrhsimopoieitai sthn dhmiourgia ths buffered image gia thn probolh ths eikonas (toBufferedImage).
    public int getPixel() {
        return pixel;
    }
    
    public short getRed() {
        return (short)((pixel>>16) & 255);
    }
    
    public short getGreen() {
        return (short)((pixel>>8) & 255);
    }
    
    public short getBlue() {
        return (short)((pixel) & 255);
    }
    
    public void setRed(short red) {
        pixel = (red<<16)| (getGreen()<<8) | (getBlue());
    }
    
    public void setGreen(short green) {
        pixel = (getRed()<<16)| (green<<8) | (getBlue());
    }
    
    public void setBlue(short blue) {
        pixel = (getRed()<<16)| (getGreen()<<8) | (blue);
    }
    
   public String toString() {
        return "Red:"+getRed()+"\nGreen:"+getGreen()+"\nBlue:"+getBlue();
    } 
}
