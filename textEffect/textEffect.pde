void setup() {
  size(800, 800);
}

void draw() {
  background(255);

  fill(0);
  textSize(width / 5);
  text("hoge", width / 2, height /2);

  loadPixels();

  int pastPixels[] = new int [width * height];
  for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
      pastPixels[y * width + x] = pixels[y * width + x];
    }
  }

  background(255);

  loadPixels();
  for (int y = height / 4; y < height * 3/4; y++) {
    for (int x = width / 4; x < width * 3/4; x++) {
      int tempX = (int)map(x, width / 4, width * 3/ 4, 0, width);
      int tempY = (int)map(y, height / 4, height * 3/ 4, 0, height);
      pixels[tempY * width + tempX] = pastPixels[y * width + x];
    }
  }

  updatePixels();
}