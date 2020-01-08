package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FrequencyMapFinder {
    static int width = 240;
    static int height = 180;
    static int maxScale = 10;
    static int scale = 4;

    long[][] frequencyMap = new long[width][height];
    byte[][] normalizedMap = new byte[width][height];
    long maxSoFar = 0;
    ArrayList<SelectionCircle> selections = new ArrayList<>(30);
SelectionCircle currentSelection;

    void openFile(String filename) {
        //  filename= "/home/arturs/Downloads/DVS LEDs/2019-11-21_16-42-50.rawdvs.aedat";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fis.skip(36);// skip header text
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] arr = new byte[8];
        int counter = 10000;
        while (true) {//(--counter > 0) {

            try {
                int res = fis.read(arr);
                if (res == -1) break;// end of file
                // System.out.print(arr[7]+" "+arr[6]+" "+arr[5]+" "+arr[4]+" "+arr[3]+" "+arr[2]+" "+arr[1]+" "+arr[0]);
                // System.out.println();
                int time = (((int) arr[4]) << 24) | ((((int) arr[5]) << 16) & 0x00FF0000) | ((((int) arr[6]) << 8) & 0x0000ff00) | (((int) arr[7]) & 0x000000ff);
                //   int time =(((int)arr[4])<<24)|(((int)arr[5])<<16)|(((int)arr[6])<<8)|((int)arr[7]);
                int y = ((arr[0] & 0x0000007f) << 2) | (((arr[1]) >> 6) & 0x00000003);
                int x = (((arr[1] & 0x00000003f)) << 4) | (((arr[2]) >> 4) & 0x0000000f);
                if (x > 0 && x < width && y > 0 && y < height) {
                    frequencyMap[x][y]++;
                    //System.out.println(time + "  y:" + y + "  x:" + x);
                    if (maxSoFar < frequencyMap[x][y]) maxSoFar = frequencyMap[x][y];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void zoomIn() {
        if (scale < maxScale)
            scale++;

    }

    void zoomOut() {
        if (scale > 1)
            scale--;

    }

    void onClickAt(double x, double y) {
        unselectAll();
        SelectionCircle potentialSel = getCircle(x, y);
        if (potentialSel == null) {// add new circle
            potentialSel = new SelectionCircle(x, y);
            selections.add(potentialSel);
        }// select circle
        potentialSel.isSelected = true;
        currentSelection=potentialSel;
    }

    SelectionCircle getCircle(double x, double y) {
        for (SelectionCircle sc : selections) {
//            if ((sc.x + sc.radius) > x && (sc.x - sc.radius) < x && (sc.y + sc.radius) > y && (sc.y - sc.radius) < y) {
                if ((sc.x + SelectionCircle.staticRadius) > x && (sc.x - SelectionCircle.staticRadius) < x && (sc.y + SelectionCircle.staticRadius) > y && (sc.y - SelectionCircle.staticRadius) < y) {

                    return sc;
            }
        }
        return null;
    }

    void unselectAll() {
        for (SelectionCircle sc : selections) {
            sc.isSelected = false;
        }
    currentSelection=null;
    }

    void moveButtonsX(int delta){
        if(currentSelection== null)return;
        currentSelection.x+= delta;///(double)scale;
    }
    void moveButtonsY(int delta){
        if(currentSelection== null)return;
        currentSelection.y+= delta;///(double)scale;
    }

    void normalize(byte value) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                normalizedMap[i][j] = (byte) (value * frequencyMap[i][j] / maxSoFar);
                if (normalizedMap[i][j] < 0) {
                    System.out.println(frequencyMap[i][j] + " maxsofar:" + maxSoFar + " res:" + normalizedMap[i][j]);
                    normalizedMap[i][j] = 0;
                }
            }
        }
    }

    void draw(Canvas canvas) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                g.setFill(Color.hsb(1, 0, normalizedMap[i][j] / 127f));
                g.fillRect((width - i) * scale, (height - j) * scale, scale, scale);
            }
        }
        for (SelectionCircle sc : selections) {
            sc.draw(g, scale);
        }
    }

}
