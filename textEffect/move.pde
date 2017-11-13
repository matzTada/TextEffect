class Movable{
  int x, y, ex, ey;
  boolean moveFlag;
  int [][] sprite;

  void setSprite(int [][] _sprite){
    sprite = new int[_sprite.length][_sprite[0].length];
    for(int j = 0; j < _sprite.length; j++){
      for(int i = 0; i < _sprite[0].length; i++){
        sprite[j][i] = _sprite[j][i];
      }
    }
  }

  void setCurrentPos(int _x, int _y){
    x = _x;
    y = _y;
  }

  void setTargetPos(int _ex, int _ey){
    ex = _ex;
    ey = _ey;
    moveFlag = true;
  }

  void moveManhattanStep(int _s){
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

  void show(color _bgColor){
    drawPixels(sprite, x, y, _bgColor);
  }
}