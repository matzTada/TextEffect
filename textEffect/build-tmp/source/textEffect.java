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



ArrayList<Movable> momos = new ArrayList<Movable>();
int cellSize;
int backgroundColor;

public void setup() {
  size(400, 400);
  colorMode(RGB, 256);
  background(color(255, 255, 255, 255));

  int charSize = width / 8;
  int cnt = 0;
  textAlign(CENTER, CENTER);
  textSize(charSize);
  colorMode(HSB, 360, 100, 100);
  noStroke();
  for (int y = 0; y < height; y += charSize) {
    for (int x = 0; x < width; x += charSize) {
      fill(random(0, 360), 100, 100);
      text("" + PApplet.parseChar('!' + cnt++), x + charSize / 2, y + charSize / 2);
    }
  }
  colorMode(RGB, 256);

  loadPixels();
  backgroundColor = pixels[0 * width + 0]; //initialize background color

  // drawPixels(copyPixelsAsRandomizedPattern(getPastPixels(), 0, 0, width/2, height/2, 4, 4), width/2, 0, color(255, 255, 255));
  // drawPixels(copyPixelsAsRandomizedPattern(getPastPixels(), 0, 0, width/2, height/2, 8, 8), 0, height/2, color(255, 255, 255));
  // drawPixels(copyPixelsAsRandomizedPattern(getPastPixels(), 0, 0, width/2, height/2, 16, 16), width/2, height/2, color(255, 255, 255));

  cellSize = charSize / 2;

  ArrayList<int []> poss = new ArrayList<int []>();
  for(int j = 0; j < height; j += cellSize){
    for(int i = 0; i < width; i += cellSize){
      poss.add(new int[]{i, j});
    }
  }
  Collections.shuffle(poss);

  for(int j = 0; j < height; j += cellSize){
    for(int i = 0; i < width; i += cellSize){
      int targetx = poss.get(poss.size()-1)[0];
      int targety = poss.get(poss.size()-1)[1];
      poss.remove(poss.size() - 1);

      Movable tempmo = new Movable();
      tempmo.setSprite(copyPixels(getPastPixels(), i, j, cellSize, cellSize));
      tempmo.setInitPos(i, j);
      tempmo.setCurrentPos(targetx, targety);
      tempmo.setTargetPos(i, j);
      momos.add(tempmo);
    }
  }
}

public void draw() {
  background(250);
  for(Movable tempmo : momos){  
    if(tempmo.moveFlag == false){
      fill(color(255,255,255,255));
      rect(tempmo.x, tempmo.y, cellSize, cellSize);
    }
  }
  for(Movable tempmo : momos){  
    // tempmo.moveManhattanStep(1, width/16); // static speed 
    // tempmo.moveManhattanStep(tempmo.initDistance / 50, width/8); // static speed based on initDistance (the farer, the faster)
    tempmo.moveManhattanStep(tempmo.getCurrentDistance() / 100, 1); // dynamic speed based on currentDistance (the farer, the faster)
    tempmo.show(backgroundColor);
  }
}

public void keyPressed() {  
  switch(key) {
    case 'i': // targeting initial position
      for(Movable tempmo : momos){
        tempmo.setTargetPos(tempmo.ix, tempmo.iy);
      }
      break;
    case 'r': // shuffle (current position to target position)
      ArrayList<int []> poss = new ArrayList<int []>();
      for(int j = 0; j < height; j += cellSize){
        for(int i = 0; i < width; i += cellSize){
          poss.add(new int[]{i, j});
        }
      }
      Collections.shuffle(poss);

      for(Movable tempmo : momos){
        int targetx = poss.get(poss.size()-1)[0];
        int targety = poss.get(poss.size()-1)[1];
        poss.remove(poss.size() - 1);
        tempmo.setTargetPos(targetx, targety);
      }
      break;
    case 's': // save current Image 
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

public void drawPixels(int pastPixels[][], int px, int py, int _bgColor) {
  loadPixels();
  for (int y = 0; y < pastPixels.length; y++) {
    for (int x = 0; x < pastPixels[0].length; x++) {
      int tempX = x + px;
      int tempY = y + py;
      if (tempX < 0 || width <= tempX) continue;
      if (tempY < 0 || height <= tempY) continue;
      if(pastPixels[y][x] != _bgColor){        
        pixels[tempY * width + tempX] = pastPixels[y][x];
      }
    }
  }
  updatePixels();
}
class Movable{
  int x, y; // current position
  int tx, ty; // target position
  int ix, iy; // initial position 
  boolean moveFlag;
  int [][] sprite;
  int vx, vy;
  int initDistance;

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
    y =  _y;
  }

  public void setTargetPos(int _tx, int _ty){
    tx = _tx;
    ty = _ty;
    initDistance = abs(tx - x) + abs (ty - y);
    moveFlag = true;
  }

  public void setInitPos(int _ix, int _iy){
    ix = _ix;
    iy = _iy;
  }

  public int getCurrentDistance(){
    return abs(tx - x) + abs (ty - y);
  }

  public void moveManhattanStep(int _s, int _block){
    int minStep = 1;
    if(_s < minStep) _s = minStep; 
    for(int s = 0; s < _s; s++){
      if(x == tx && y == ty){
        moveFlag = false;
        return;
      }
      else if(x == tx && y != ty){
        vx = 0;
        vy = 1 * (ty - y) / abs(ty - y);       
      }
      else if(x != tx && y == ty){
        vx = 1 * (tx - x) / abs(tx - x);
        vy = 0;
      }
      else if(x % _block == 0 || y % _block == 0){
        if(random(0, 100) > 50){
          vx = 1 * (tx - x) / abs(tx - x);
          vy = 0;
        }else{
          vx = 0;
          vy = 1 * (ty - y) / abs(ty - y);
        }
      }

      x += vx;
      y += vy;        
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
