class Movable{
  int x, y; // current position
  int tx, ty; // target position
  int ix, iy; // initial position 
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

  void setTargetPos(int _tx, int _ty){
    tx = _tx;
    ty = _ty;
    initDistance = abs(tx - x) + abs (ty - y);
    moveFlag = true;
  }

  void setInitPos(int _ix, int _iy){
    ix = _ix;
    iy = _iy;
  }

  int getCurrentDistance(){
    return abs(tx - x) + abs (ty - y);
  }

  void moveManhattanStep(int _s, int _block){
    int minStep = 2;
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

  void show(color _bgColor){
    drawPixels(sprite, x, y, _bgColor);
  }
}