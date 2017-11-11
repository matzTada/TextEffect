void setup() {
  size(1600, 1600);
  background(255);

  fill(0);
  textSize(width / 5);
  textAlign(LEFT, TOP);
  text("hoge", 0, 0);

  //rect(0, 0, width/2, height);

  int dice = 25;
  for (int y = 0; y < height/2; y += dice) {
    for (int x = 0; x < width/2; x += dice) {
      if ((x+y)% (dice*2)==0) {
        movePixels(x, y, dice, dice, color(255, 0, 0), width/2 + x, height/2 + y);
      }
    }
  }
}

void draw() {
}

void keyPressed() {  
  int pastPixels[][] = getPastPixels();
  int zoomed[][] = getPastPixels();  
  switch(key) {
  case 's':
    zoomed = slidePixels(pastPixels, 0, width, height * 31/64, height * 33/64);
    drawPixels(zoomed, width / 16, height * 31/64);
    break;
  case 'z':
    zoomed = zoomPixels(pastPixels, 0, width, 0, height, width * 2, height * 2);
    drawPixels(zoomed, -width / 2, -height/ 2);
    break;
  }
  redraw();
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