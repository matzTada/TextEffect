void setup() {
  size(1600, 1600);
  background(255);

  fill(0);
  textSize(width / 5);
  textAlign(CENTER, CENTER);
  text("hoge", width / 2, height /2);

  int pastPixels[] = getPastPixels();
  int zoomed[][] = zoomPixels(pastPixels, width / 4, width * 3 / 4, height / 4, height * 3 / 4, width, height);

  background(255);
  loadPixels(); // reset pixels
  drawPixels(zoomed, 0, 0, width, height);
}

void draw() {
}

int [] getPastPixels() {
  int ra [] = new int [width * height];
  loadPixels();
  for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
      ra[y * width + x] = pixels[y * width + x];
    }
  }
  return ra;
}


int [][] zoomPixels(int pastPixels[], int lx, int rx, int ly, int ry, int w, int h) {
  int [][] ra = new int[h][w];
  for (int y = ly; y < ry; y++) {
    for (int x = lx; x < rx; x++) {
      int tempX = (int)map(x, lx, rx, 0, w);
      int tempY = (int)map(y, ly, ry, 0, h);
      ra[tempY][tempX] = pastPixels[y * w + x];
    }
  }
  return ra;
}

void drawPixels(int pastPixels[][], int px, int py, int w, int h) {
  for (int y = 0; y < pastPixels.length; y++) {
    for (int x = 0; x < pastPixels[0].length; x++) {
      int tempX = (int)map(x, 0, pastPixels[0].length, px, px + w);
      tempX = constrain(tempX, 0, width);
      int tempY = (int)map(y, 0, pastPixels.length, py, py + h);
      tempY = constrain(tempY, 0, height);
      pixels[tempY * width + tempX] = pastPixels[y][x];
    }
  }
  updatePixels();
}