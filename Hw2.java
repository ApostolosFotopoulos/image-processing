package ce325.hw2;

/*To programma anoigei mia eikona pou einai typou YUV h PPM h JPG kai thn probalei
se ena parathyro.To parathyro periexei ena menou me to opoio o xrhsths mporei na
tropopoihsei thn eikona (na thn kanei aspromaurh,na thn peristrepsei klp) kai meta
na thn apothikeusei eite ws mia PPM eikona eite ws YUV eikona.Epipleon mporei 
na afairesei ton thorybo apo mia seira apo eikones kai na paraxei mia eikona pou
den exei thorybo.Mporei epishs na isoroppisei to istogramma mias eikonas kai na paraxei
mia nea eikona sthn opoia einai pio eudiakrita orismena shmeia.Tautoxrona paragei 
ena txt arxeio pou gia kathe timh fwteinothtas exei tous analogous "*" xarakthres.
Telos mporei na kanei reset thn eikona ystera apo thn tropoihsh ths.*/
public class Hw2 {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ImageProcessing imageProcessing = new ImageProcessing();
            }
        });
        
    }  
}