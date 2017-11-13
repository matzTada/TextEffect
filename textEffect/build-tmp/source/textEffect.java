import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class textEffect extends PApplet {



Movable momo = new Movable();

public void setup() {
  size(800, 800);
  background(255);

  int cellSize = width / 4;
  int cnt = 0;
  textAlign(CENTER, CENTER);
  textSize(cellSize);
  colorMode(HSB, 360, 100, 100);
  for (int y = 0; y < height/2; y += cellSize) {
    for (int x = 0; x < width/2; x += cellSize) {
      fill(random(0, 360), 100, 100);
      text("" + PApplet.parseChar('A' + cnt++), x + cellSize / 2, y + cellSize / 2);
    }
  }
  colorMode(RGB, 256);

  // drawPixels(copyPixelsAsRandomizedPattern(getPastPixels(), 0, 0, width/2, height/2, 4, 4), width/2, 0, color(255, 255, 255));
  // drawPixels(copyPixelsAsRandomizedPattern(getPastPixels(), 0, 0, width/2, height/2, 8, 8), 0, height/2, color(255, 255, 255));
  // drawPixels(copyPixelsAsRandomizedPattern(getPastPixels(), 0, 0, width/2, height/2, 16, 16), width/2, height/2, color(255, 255, 255));

  momo.setSprite(copyPixels(getPastPixels(), 0, 0, 200, 200));
  momo.setCurrentPos(0, 0);
  momo.setTargetPos(600, 600);
}

public void draw() {
  background(255);
  momo.moveManhattanStep(10);
  momo.show(color(255, 255, 255));
  delay(10);
}

public void keyPressed() {  
  switch(key) {
  case 's': //save current Image 
    String saveImageFileName = year() + "_" + month() + "_" + day() + "_" +hour() + "_" +minute() + "_" +second() + ".jpg";
    println("========== saved image: " + saveImageFileName + " ==========");
    save(saveImageFileName); //save image for offline
    break;
  }
}

public int [][] copyPixels(int pastPixels[][], int px, int py, int dx, int dy) {
  int ra [][] = new int [dx][dy];
  for (int y = py; y < py + dy; y++) {
    for (int x = px; x < px + dx; x++) {
      ra[y-py][x-px] = pastPixels[y][x];
    }
  }
  return ra;
}

public int [][] copyPixelsAsStripedHorizontalPattern(int pastPixels[][], int px, int py, int dx, int dy, int w) {
  int ra [][] = new int [dx][dy];
  for (int celly = py; celly < py + dy; celly += w) {
    for (int cellx = px; cellx < px + dx; cellx += w) {
      if ((celly)% (w*2)==0) {
        for (int y = celly; y < celly + w && y < py + dy; y ++) {
          for (int x = cellx; x < cellx + w && x < px + dx; x ++) {
            ra[y-py][x-px] = pastPixels[y][x];
          }
        }
      }
    }
  }
  return ra;
}

public int [][] copyPixelsAsStripedVerticalPattern(int pastPixels[][], int px, int py, int dx, int dy, int w) {
  int ra [][] = new int [dx][dy];
  for (int celly = py; celly < py + dy; celly += w) {
    for (int cellx = px; cellx < px + dx; cellx += w) {
      if ((cellx)% (w*2)==0) {
        for (int y = celly; y < celly + w && y < py + dy; y ++) {
          for (int x = cellx; x < cellx + w && x < px + dx; x ++) {
            ra[y-py][x-px] = pastPixels[y][x];
          }
        }
      }
    }
  }
  return ra;
}

public int [][] copyPixelsAsCheckeredPattern(int pastPixels[][], int px, int py, int dx, int dy, int w) {
  int ra [][] = new int [dx][dy];
  for (int celly = py; celly < py + dy; celly += w) {
    for (int cellx = px; cellx < px + dx; cellx += w) {
      if ((cellx+celly)% (w*2)==0) {
        for (int y = celly; y < celly + w && y < py + dy; y ++) {
          for (int x = cellx; x < cellx + w && x < px + dx; x ++) {
            ra[y-py][x-px] = pastPixels[y][x];
          }
        }
      }
    }
  }
  return ra;
}
public int [][] copyPixelsAsRandomizedPattern(int pastPixels[][], int px, int py, int dx, int dy, int numx, int numy) {
  int wx = dx / numx;
  int wy = dy / numy;
  ArrayList<int [][]> cells = new ArrayList<int [][]>();
  for (int celly = py; celly < py + dy; celly += wy) {
    for (int cellx = px; cellx < px + dx; cellx += wx) {
      int ta [][] = new int [wy][wx];    
      for (int y = celly; y < celly + wy && y < py + dy; y ++) {
        for (int x = cellx; x < cellx + wx && x < px + dx; x ++) {
          ta[y-celly][x-cellx] = pastPixels[y][x];
        }
      }
      cells.add(ta);
    }
  }

  Collections.shuffle(cells);

  int ra [][] = new int [dx][dy];

  int cnt = 0;
  for (int celly = py; celly < py + dy; celly += wy) {
    for (int cellx = px; cellx < px + dx; cellx += wx) {
      int ta [][] = cells.get(cnt);
      for (int y = 0; y < wy && y+celly < dy; y ++) {
        for (int x = 0; x < wx && x + cellx < dx; x ++) {
          ra[y+celly][x+cellx] = ta[y][x];
        }
      }
      cnt++;
    }
  }  

  return ra;
}


public int [][] fillPixels(int dx, int dy, int c) {
  int ra [][] = new int [dx][dy];

  for (int y = 0; y < dy; y++) {
    for (int x = 0; x < dx; x++) {
      ra[y][x] = c;
    }
  }
  return ra;
}

public int [][] getPastPixels() {
  int ra [][] = new int [height][width];
  loadPixels();
  for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
      ra[y][x] = pixels[y * width + x];
    }
  }
  return ra;
}


public int [][] slidePixels(int pastPixels[][], int lx, int rx, int ly, int ry) {
  int [][] ra = new int[ry - ly][rx - lx];
  for (int y = ly; y < ry; y++) {
    for (int x = lx; x < rx; x++) {
      int tempX = x - lx;
      int tempY = y - ly;
      ra[tempY][tempX] = pastPixels[y][x];
    }
  }
  return ra;
}

public int [][] zoomPixels(int pastPixels[][], int lx, int rx, int ly, int ry, int w, int h) {
  int [][] ra = new int[h][w];
  for (int y = ly; y < ry; y++) {
    for (int x = lx; x < rx; x++) {
      int tempX = (int)map(x, lx, rx, 0, w);
      int tempY = (int)map(y, ly, ry, 0, h);
      ra[tempY][tempX] = pastPixels[y][x];
    }
  }
  return ra;
}

public void drawPixels(int pastPixels[][], int px, int py, int bgColor) {
  loadPixels();
  for (int y = 0; y < pastPixels.length; y++) {
    for (int x = 0; x < pastPixels[0].length; x++) {
      int tempX = x + px;
      int tempY = y + py;
      if (tempX < 0 || width <= tempX) continue;
      if (tempY < 0 || height <= tempY) continue;
      if(pastPixels[y][x] != bgColor){
        pixels[tempY * width + tempX] = pastPixels[y][x];
      }
    }
  }
  updatePixels();
}
class Movable{
  int x, y, ex, ey;
  boolean moveFlag;
  int [][] sprite;

  public void setSprite(int [][] _sprite){
    sprite = new int[_sprite.length][_sprite[0].length];
    for(int j = 0; j < _sprite.length; j++){
      for(int i = 0; i < _sprite[0].length; i++){
        sprite[j][i] = _sprite[j][i];
      }
    }
  }

  public void setCurrentPos(int _x, int _y){
    x = _x;
    y = _y;
  }

  public void setTargetPos(int _ex, int _ey){
    ex = _ex;
    ey = _ey;
    moveFlag = true;
  }

  public void moveManhattanStep(int _s){
    for(int s = 0; s < _s; s++){
      if((ex - x != 0) && (ey - y != 0)){
        if(random(0, 100) > 50){
          x += 1 * (ex - x) / abs(ex - x);
        }else{
          y += 1 * (ey - y) / abs(ey - y);
        }
      }
      else if((ex - x == 0) && (ey - y != 0)){
        y += 1 * (ey - y) / abs(ey - y);
      } 
      else if((ey - y == 0) && (ex - x != 0)){
        x += 1 * (ex - x) / abs(ex - x);
      }else{
        moveFlag = false;
        return;
      }
    }
  }

  public void show(int _bgColor){
    drawPixels(sprite, x, y, _bgColor);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "textEffect" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
