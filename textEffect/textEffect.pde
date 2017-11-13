import java.util.*;

void setup() {
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
      text("" + char('A' + cnt++), x + cellSize / 2, y + cellSize / 2);
    }
  }
  colorMode(RGB);
}

int cellNum = 32;
void draw() {
  fill(255);
  rect(width/2, 0, width/2, height/2);
  fill(0);
  text(cellNum, width * 3 / 4, height * 1 / 4);
  drawPixels(copyPixelsAsRandomizedPattern(getPastPixels(), 0, 0, width/2, height/2, cellNum, cellNum), width/2, height/2);
  cellNum /= 2;
  if (cellNum < 1) cellNum = 32;
  delay(500);
}

void keyPressed() {  
  switch(key) {
  case 's': //save current Image 
    String saveImageFileName = year() + "_" + month() + "_" + day() + "_" +hour() + "_" +minute() + "_" +second() + ".jpg";
    println("========== saved image: " + saveImageFileName + " ==========");
    save(saveImageFileName); //save image for offline
    break;
  }
}

int [][] copyPixels(int pastPixels[][], int px, int py, int dx, int dy) {
  int ra [][] = new int [dx][dy];
  for (int y = py; y < py + dy; y++) {
    for (int x = px; x < px + dx; x++) {
      ra[y-py][x-px] = pastPixels[y][x];
    }
  }
  return ra;
}

int [][] copyPixelsAsStripedHorizontalPattern(int pastPixels[][], int px, int py, int dx, int dy, int w) {
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

int [][] copyPixelsAsStripedVerticalPattern(int pastPixels[][], int px, int py, int dx, int dy, int w) {
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

int [][] copyPixelsAsCheckeredPattern(int pastPixels[][], int px, int py, int dx, int dy, int w) {
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
int [][] copyPixelsAsRandomizedPattern(int pastPixels[][], int px, int py, int dx, int dy, int numx, int numy) {
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


int [][] fillPixels(int dx, int dy, color c) {
  int ra [][] = new int [dx][dy];

  for (int y = 0; y < dy; y++) {
    for (int x = 0; x < dx; x++) {
      ra[y][x] = c;
    }
  }
  return ra;
}

void movePixels(int cx, int cy, int dx, int dy, color c, int px, int py) {
  drawPixels(copyPixels(getPastPixels(), cx, cy, dx, dy), px, py);
  drawPixels(fillPixels(dx, dy, c), cx, cy);
}

int [][] getPastPixels() {
  int ra [][] = new int [height][width];
  loadPixels();
  for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
      ra[y][x] = pixels[y * width + x];
    }
  }
  return ra;
}


int [][] slidePixels(int pastPixels[][], int lx, int rx, int ly, int ry) {
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

int [][] zoomPixels(int pastPixels[][], int lx, int rx, int ly, int ry, int w, int h) {
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

void drawPixels(int pastPixels[][], int px, int py) {
  for (int y = 0; y < pastPixels.length; y++) {
    for (int x = 0; x < pastPixels[0].length; x++) {
      int tempX = x + px;
      int tempY = y + py;
      if (tempX < 0 || width <= tempX) continue;
      if (tempY < 0 || height <= tempY) continue;
      pixels[tempY * width + tempX] = pastPixels[y][x];
    }
  }
  updatePixels();
}