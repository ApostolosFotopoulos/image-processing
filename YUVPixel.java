package ce325.hw2;

public class YUVPixel {
    private int pixel;
    
    //Kataskeuasths pou dhmiourgei ena pixel me tis times rgb.
    public YUVPixel(short Y, short U, short V) {
        pixel = (Y<<16) | (U<<8) | (V);
    }
    
    //Kataskeuasths pou dhmiourgei ena antigrapho enos pixel.
    public YUVPixel(YUVPixel pixel) {
        this.pixel = (pixel.getY()<<16) | (pixel.getU()<<8) | (pixel.getV());
    }
    
    //Kataskeuasths pou metatrepei ena RGB pixel se kwdikopoihsh YUV.
    public YUVPixel(RGBPixel pixel) {
        short Y = (short) (( ( 66 * pixel.getRed() + 129 * pixel.getGreen() + 25 * pixel.getBlue() + 128) >> 8) + 16);
        short U = (short) (( ( -38 * pixel.getRed() - 74 * pixel.getGreen() + 112 * pixel.getBlue() + 128) >> 8) + 128);
        short V = (short) (( ( 112 * pixel.getRed() - 94 * pixel.getGreen() - 18 * pixel.getBlue() + 128) >> 8) + 128);
        this.pixel = (Y<<16) | (U<<8) | (V);
    }
    
    public short getY() {
        return (short)((pixel>>16) & 255);
    }
    
    public short getU() {
        return (short)((pixel>>8) & 255);
    }
    
    public short getV() {
        return (short)((pixel) & 255);
    }
    
    public void setY(short Y) {
        pixel = (Y<<16)| (getU()<<8) | (getV());
    }
    
    public void setU(short U) {
        pixel = (getY()<<16)| (U<<8) | (getV());
    }
    
    public void setV(short V) {
        pixel = (getY()<<16)| (getU()<<8) | (V);
    }
    
   public String toString() {
        return "Y:"+getY()+"\nU:"+getU()+"\nV:"+getV();
    } 
}
