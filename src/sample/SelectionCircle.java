package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SelectionCircle {
   static int staticRadius =3;
    static int maxRadius =10;

    double x;
    double y;
    double radius = 3;
boolean isSelected = false;

    public SelectionCircle(double x, double y) {
        this.x = x;
        this.y = y;
    }

   void draw(GraphicsContext g, int scale){
       if(!isSelected)
           g.setStroke(Color.GREEN);
       else
           g.setStroke(Color.RED);

   //    g.strokeOval((x-radius)*scale,(y-radius)*scale,(2*radius)*scale,(2*radius)*scale);
       g.strokeOval((x-staticRadius)*scale,(y-staticRadius)*scale,(2*staticRadius)*scale,(2*staticRadius)*scale);

   }
 static void  changeStaticRadius(int delta){
        int peekRadi = staticRadius+ delta;
        if(peekRadi<1|| peekRadi>maxRadius) return;
        staticRadius=peekRadi;

  }
}
