class Movable{
  int x, y, ex, ey;
  boolean moveFlag;
  int [][] sprite;
  int vx, vy;
  int initDistance;

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
    y =  _y;
  }

  void setTargetPos(int _ex, int _ey){
    ex = _ex;
    ey = _ey;
    initDistance = abs(ex - x) + abs (ey - y);
    moveFlag = true;
  }

  int getCurrentDistance(){
    return abs(ex - x) + abs (ey - y);
  }

  void moveManhattanStep(int _s, int _block){
    if(_s < 1) _s = 1; 
    for(int s = 0; s < _s; s++){
      if(x == ex && y == ey){
        moveFlag = false;
        return;
      }
      else if(x == ex && y != ey){
        vx = 0;
        vy = 1 * (ey - y) / abs(ey - y);       
      }
      else if(x != ex && y == ey){
        vx = 1 * (ex - x) / abs(ex - x);
        vy = 0;
      }
      else{
        if(random(0, 100) > 50){
          vx = 1 * (ex - x) / abs(ex - x);
          vy = 0;
        }else{
          vx = 0;
          vy = 1 * (ey - y) / abs(ey - y);
        }
      }

      x += vx;
      y += vy;        
    }
  }

  void show(color _bgColor){
    drawPixels(sprite, x, y, _bgColor);
  }
}