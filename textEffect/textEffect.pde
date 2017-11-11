void setup() {
  size(1600, 1600);
  background(255);

  fill(0);
  textSize(width / 5);
  textAlign(CENTER, CENTER);
  text("hoge", width / 2, height /2);
  
  rect(0, 0, width/2, height);

  drawPixels(copyPixels(getPastPixels(),0,height/2,100,100), width/2, height/2);
  drawPixels(fillPixels(100, 100, color(255,0,0)), 0, height/2);

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

int [][] copyPixels(int pastPixels[][], int px, int py, int dx, int dy){
  int ra [][] = new int [dx][dy];
  for (int y = py; y < py + dy; y++) {
    for (int x = px; x < px + dx; x++) {
      ra[y-py][x-px] = pastPixels[y][x];
    }
  }
  return ra;
}

int [][] fillPixels(int dx, int dy, color c){
  int ra [][] = new int [dx][dy];

  for (int y = 0; y < dy; y++) {
    for (int x = 0; x < dx; x++) {
      ra[y][x] = c;
    }
  }
  return ra;
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