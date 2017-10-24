/*
 * 连发，子弹最多存在五颗
 */
package com.test4;

import java.util.Vector;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

class Bomb
{
	int x;
	int y;
	int life = 9;
//	boolean isLive = true;
	public Bomb(int x , int y)
	{
		this.x = x;
		this.y = y;
	}
	public void lifeDown()
	{
		if (this.life >0)
		{
			life--;
		}
	}
	
}
class Shot implements Runnable
{
	int x = 0;
	int y = 0;
	int direct;
	int speed = 10;
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	boolean isLive = true;
	public Shot(){}
	
	public Shot(int x , int y , int direct)
	{
		this.x = x;
		this.y = y;
		this.direct = direct;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(direct)
			{
			case 0:
				y-=speed;
				break;
			case 1:
				y+=speed;
				break;
			case 2:
				x-=speed;
				break;
			case 3:
				x+=speed;
				break;
			}
//			System.out.println("子弹的坐标：（" + x +"," + y +")");
			
			//if判断语句中this.isLive ==false
			//为了消除击中坦克后子弹消失但是下面显示的子弹坐标仍然到边界后才消失的现象
			//以上为自行添加，尚有疑问
			
			if(y<=0||x<=0||y>=300||x>=400)
			{
				this.isLive = false;
				break;
			}
		}
	}
}
class Tank
{
	int x = 0;
	int y = 0;
	int direct = 0;
	int speed = 1;
	int color;
	boolean isLive = true;
	

	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public Tank(){}
	public Tank(int x , int y)
	{
		this.x = x;
		this.y = y;
	}

}

class Hero extends Tank
{
	//speed
	int speed = 2;
	public Hero(){}
public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	//	Shot s = null;
	Vector<Shot> ss = new Vector<>();
	Shot s = null;
//	boolean isLive = true;
	public void shotEnermy()
	{
	
		switch(this.direct){
		case 0:
			s = new Shot(x + 10 , y , 0);
			ss.add(s);
			break;
		case 1:
			s = new Shot(x + 10 , y + 30 , 1);
			ss.add(s);
			break;
		case 2:
			s = new Shot (x , y + 10 , 2);
			ss.add(s);
			break;
		case 3:
			s = new Shot(x + 30 , y + 10 ,3);
			ss.add(s);
			break;
		}
		
		Thread t = new Thread(s);
		t.start();
	}
	public Hero(int x, int y)
	{
		super(x , y);
	}
	public void moveUp()
	{
		if(y > 0)
		{
			y -= speed;
		}
	}
	public void moveDown()
	{
		if (y < 270)
		{
			y += speed;
		}
	}
	public void moveLeft()
	{
		if (x > 0)
		{
			x -= speed;
		}
	}
	public void moveRight()
	{
		if (x < 370)
		{
			x += speed;
		}
	}
}

class EnemyTank extends Tank implements Runnable
{
//	boolean isLive = true;
	Vector<Shot> ss = new Vector<>();
	//定义一个访问其他坦克的向量
	public EnemyTank(){}
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	public void getEts(Vector<EnemyTank> vv){
		this.ets = vv;
	}
	
	int times = 0;
	public EnemyTank(int x , int y)
	{
		super(x , y);
	}
	
	public boolean isTouchOtherTank()
	{
		//判断是否与其他坦克相撞
		boolean b = false;
		//...
		switch(this.direct)
		{
		case 0:
			//方向向上
			for (int i = 0 ; i < ets.size() ; i ++)
			{
				EnemyTank et = ets.get(i);
				if (et != this)
				{
				if (et.direct == 0 || et.direct == 1)
				{
					if(this.x>=et.x && this.x<=et.x+20 && this.y>=et.y && this.y<=et.y+30)
                    {
                        return true;
                    }
                    if(this.x+20>=et.x && this.x+20<=et.x+20 && this.y>=et.y && this.y<=et.y+30)
                    {
                        return true;
                    }
				}
				if (et.direct == 2 && et.direct == 3)
				{
					if(this.x>=et.x && this.x<=et.x+30 && this.y>=et.y && this.y<=et.y+20)
                    {
                        return true;
                    }
                    if(this.x+20>=et.x && this.x+20<=et.x+30 && this.y>=et.y && this.y<=et.y+20)
                    {
                        return true;
                    }
				}
				}
			}
			break;
		case 1:
			for (int i = 0 ; i < ets.size() ; i ++)
			{
				EnemyTank et = ets.get(i);
				if (et != this)
				{
				if (et.direct == 0 || et.direct == 1)
				{
					if(this.x>=et.x && this.x<=et.x+20 && this.y+30>=et.y && this.y+30<=et.y+30)
                    {
                        return true;
                    }
                    if(this.x+20>=et.x && this.x+20<=et.x+20 && this.y+30>=et.y && this.y+30<=et.y+30)
                    {
                        return true;
                    }
				}
				if (et.direct == 2 && et.direct == 3)
				{
					if(this.x>=et.x && this.x<=et.x+30 && this.y+30>=et.y && this.y+30<=et.y+20)
                    {
                        return true;
                    }
                    if(this.x+20>=et.x && this.x+20<=et.x+30 && this.y+30>=et.y && this.y+30<=et.y+20)
                    {
                        return true;
                    }
				}
				}
			}
			break;
		case 2:
			for(int i=0;i<ets.size();i++)
            {
                //取出第一个坦克
                EnemyTank et = ets.get(i);
                //如果不是自己
                if(et!=this)
                {
                    //如果敌人的方向是向下或者向上
                    if(et.direct==0 || et.direct==1)
                    {
                        if(this.x>=et.x && this.x<=et.x+20 && this.y>=et.y && this.y<=et.y+30)
                        {
                            return true;
                        }
                        if(this.x>=et.x && this.x<=et.x+20 && this.y+20>=et.y && this.y+20<=et.y+30)
                        {
                            return true;
                        }
                    }
                    if(et.direct==2 || et.direct==3)
                    {
                        if(this.x>=et.x && this.x<=et.x+30 && this.y+20>=et.y && this.y+20<=et.y+20)
                        {
                            return true;
                        }
                        if(this.x>=et.x && this.x<=et.x+30 && this.y+20>=et.y && this.y+20<=et.y+20)
                        {
                            return true;
                        }
                    }
                }
            }
			break;
		case 3:
			for(int i=0;i<ets.size();i++)
            {
                //取出第一个坦克
                EnemyTank et = ets.get(i);
                //如果不是自己
                if(et!=this)
                {
                    //如果敌人的方向是向下或者向上
                    if(et.direct==0 || et.direct==1)
                    {
                        //上点
                        if(this.x+30>=et.x && this.x+30<=et.x+20 && this.y>=et.y && this.y<=et.y+30)
                        {
                            return true;
                        }
                        //下点
                        if(this.x+30>=et.x && this.x+30<=et.x+20 && this.y+20>=et.y && this.y+20<=et.y+30)
                        {
                            return true;
                        }
                    }
                    if(et.direct==2 || et.direct==3)
                    {
                        if(this.x+30>=et.x && this.x+30<=et.x+30 && this.y>=et.y && this.y<=et.y+20)
                        {
                            return true;
                        }
                        if(this.x+30>=et.x && this.x+30<=et.x+30 && this.y+20>=et.y && this.y+20<=et.y+20)
                        {
                            return true;
                        }
                    }
                }
            }
			break;
		}
		return b;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{

		switch(this.direct)
		{
		case 0:

			for(int i = 0 ; i < 30 ; i++)
			{
				if(y>0 && !this.isTouchOtherTank())
				{
				y -= speed;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			break;
		case 1:

			for(int i = 0 ; i < 30 ; i++)
			{
				if (y<300 && !this.isTouchOtherTank())
				{
				y += speed;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			break;
		case 2:

			for(int i = 0 ; i < 30 ; i++)
			{
				if (x > 0 && !this.isTouchOtherTank())
				{
				x -= speed;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			break;
		case 3:

			for(int i = 0 ; i < 30 ; i++)
			{
				if (x < 400 && !this.isTouchOtherTank())
				{
				x += speed;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			break;
		}
		times ++;
		if (times%2 == 0)
		{
		if(this.isLive)
		{
			if(ss.size() < 5)
			{
				Shot s = null;
				switch(this.direct)
				{
				case 0:
					s = new Shot(x + 10 , y , 0);
					ss.add(s);
					break;
				case 1:
					s = new Shot(x + 10 , y + 30 , 1);
					ss.add(s);
					break;
				case 2:
					s = new Shot (x , y + 10 , 2);
					ss.add(s);
					break;
				case 3:
					s = new Shot(x + 30 , y + 10 ,3);
					ss.add(s);
					break;
				}
				if(s.isLive == false)
				{
					ss.remove(s);
				}
				Thread t = new Thread(s);
				t.start();
				if(s.isLive == false)
				{
					ss.remove(s);
				}
			}
		}
		}
		this.direct =(int) (Math.random() * 4);
		if (this.isLive == false)
		{
			break;
		}
		}
	}
}