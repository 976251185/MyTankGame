/**
 * ���ܣ�̹�˴�ս1.0
 * 1.����̹��
 * 2.�ҵ�̹�˿����ƶ�
 * 3.�з�̹�˿��������ƶ������ӵ�
 * 
 */
package com.test4;
import java.awt.*;
import java.util.Vector;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MyTankGame8 extends JFrame implements ActionListener {
	MyPanel mp = null;
	MyStartPanel msp = null;
	JMenuBar jmb = null;
	JMenu jm1 = null;
	JMenuItem jmi1 = null;
	JMenuItem jmi2 = null;
	JMenuItem jmi3 = null;
	JMenuItem jmi4 = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame8 mtg = new MyTankGame8();
	}
	public MyTankGame8()
	{
		msp = new MyStartPanel();

		this.add(msp);
		this.setSize(580, 500);
		Thread t = new Thread(msp);
		t.start();
		jmb = new JMenuBar();
		jm1 = new JMenu("Game");
		jm1.setMnemonic('G');
		this.setJMenuBar(jmb);
		jmb.add(jm1);
		jmi1 = new JMenuItem("��ʼ����Ϸ");
		jmi2 = new JMenuItem("�˳���Ϸ");
		jmi3 = new JMenuItem("�����˳�(c)");
		jmi3.setMnemonic('C');
		jmi3.addActionListener(this);
		jmi3.setActionCommand("saveExit");
		jmi4 = new JMenuItem("��ȡ�浵");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("loadGame");
		jmi2.setMnemonic('E');
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmi1.addActionListener(this);
		jmi1.setActionCommand("startgame");

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand()=="startgame")
		{
			this.remove(msp);
			try {
				mp = new MyPanel("newGame");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Thread t = new Thread(mp);
			t.start();
			this.add(mp);
			this.addKeyListener(mp);
			this.setSize(579, 500);
			this.setVisible(true);
		}
		else if(e.getActionCommand() == "exit")
		{
			//�����ݴ���
			new Recorder().keepRecording();
			System.exit(0);			
		}
		else if(e.getActionCommand() == "saveExit")
		{
			//�����˳�
			System.out.println("�����˳�");
			Recorder.setEts(mp.ets);
			new Recorder().keepRecAndEnemyTank();
			System.exit(0);
		}
		else if(e.getActionCommand() == "loadGame")
		{
			//��ȡ֮ǰ�Ĵ浵��
			System.out.println("loadgame");
			this.remove(msp);
			try {
				mp = new MyPanel("conGame");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("�������");
			Thread t = new Thread(mp);
			t.start();
			this.add(mp);
			this.addKeyListener(mp);
			this.setSize(579, 500);
			this.setVisible(true);
		}
	}
}

//MyPanel
class MyPanel extends JPanel implements KeyListener ,Runnable
{
	
	Hero hero = null;
	//������˵�̹��
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	Vector<Node> n1 = new Vector<Node>();
	int enSize =3;
	
	//ȡ����ըЧ��ͼƬ
	

	Image image1 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("bomb_1.gif"));
	Image image2 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("bomb_2.gif"));
	Image image3 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("bomb_3.gif"));
	//����ը����
	Vector<Bomb> bombs = new Vector<Bomb>();
	
	public MyPanel(String flag) throws IOException
	{
		try {
			new Recorder().getRecording();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hero = new Hero(10 , 10);
		//enemy
		if (flag.equals("newGame"))
		{
			for(int i = 0; i<enSize;i++)
			{
				//����̹�ˣ�����
				EnemyTank et = new EnemyTank((i+1)*50 , 0);
				Thread t = new Thread(et);
				t.start();
				Shot s = new Shot (et.x + 10 , et.y + 30 , 1);
				et.ss.add(s);
				Thread t1 = new Thread(s);
				t1.start();
				ets.add(et);
				//�Ѹ�tank���뵽�����ж��Ƿ���ײ��������
				et.getEts(ets);
				et.setColor(0);
				et.setDirect(1);
			}	 
		}
		else if (flag.equals("conGame"))
		{
			System.out.println("������Ϸ");
			n1 = new Recorder().getEneAndRecording();
			for(int i = 0 ; i < n1.size() ; i ++)
			{
				Node node = n1.get(i);
				EnemyTank et = new EnemyTank(node.x , node.y);
				et.setDirect(node.direct);
				Thread t = new Thread(et);
				t.start();
				Shot s = new Shot (et.x + 10 , et.y + 30 , 1);
				et.ss.add(s);
				Thread t1 = new Thread(s);
				t1.start();
				ets.add(et);
				//�Ѹ�tank���뵽�����ж��Ƿ���ײ��������
				et.setColor(0);
			}
		}
		
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(image1, 500, 500, this);
		g.drawImage(image2, 500, 500, this);
		g.drawImage(image3, 500, 500, this);
		g.fillRect(0, 0, 400, 300);
		
		//����������ʾ̹��
		this.showInfo(g);
		
		//�����ҷ�̹��
		if(hero.isLive)
		{
		this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 0);
		}
		//��������̹��
		for(int i = 0 ; i < ets.size(); i++)
		{
			EnemyTank et = ets.get(i);
			if (et.isLive){
				this.drawTank(et.x, et.y, g, et.direct, 1);
			}
			for(int j = 0 ; j < et.ss.size() ; j ++)
			{
				Shot myShot = et.ss.get(j);
				if (myShot.isLive == true)
				{
					g.draw3DRect(myShot.x , myShot.y , 1, 1, false);
				}
				else
				{
					et.ss.remove(myShot);
				}
			}
		}
		//�����ӵ�
		for(int i = 0 ; i < hero.ss.size(); i++)
		{
			Shot myShot = hero.ss.get(i);
			
			if (myShot!= null && myShot.isLive == true)
			{
				g.draw3DRect(myShot.x , myShot.y , 1, 1, false);
			}
			if(myShot.isLive == false)
			{
				hero.ss.remove(myShot);
			}
		}
		//������ըЧ��
		for(int i = 0 ; i <bombs.size() ; i ++)
		{

			System.out.println("bombs.size() = " + bombs.size());
			Bomb b = bombs.get(i);
			if (b.life > 6)
			{
				g.drawImage(image1, b.x, b.y, 30 , 30 , this);		
			}
			else if(b.life > 3)
			{
				g.drawImage(image2, b.x, b.y, 30, 30, this);			
			}
			else
			{
				g.drawImage(image3, b.x, b.y, 30 , 30, this);
			}
			b.lifeDown();
			if(b.life == 0)
			{
				bombs.remove(b);
			}
		}
	}
	//����̹�˵Ĳ���
	public void drawTank(int x , int y, Graphics g , int direct, int type)
	{
		switch(type)
		{
		case 0:
			g.setColor(Color.cyan);
			break;
		case 1:
			g.setColor(Color.YELLOW);
			break;
		}
		switch(direct)
		{
		case 0:
			
			g.fill3DRect(x, y, 5, 30, false);
			g.fill3DRect(x+15, y, 5, 30, false);
			g.fill3DRect(x+5, y+5, 10, 20, false);
			g.fillOval(x + 5, y + 10, 10, 10);
			g.drawLine(x + 10, y + 15, x+10, y);
			break;
		case 1:
			g.fill3DRect(x, y, 5, 30, false);
			g.fill3DRect(x+15, y, 5, 30, false);
			g.fill3DRect(x+5, y+5, 10, 20, false);
			g.fillOval(x + 5, y + 10, 10, 10);
			g.drawLine(x + 10, y + 15, x+10, y + 30);
			break;
		case 2:
			g.fill3DRect(x, y, 30, 5, false);
			g.fill3DRect(x, y + 15, 30, 5, false);
			g.fill3DRect(x + 5 , y + 5, 20, 10, false);
			g.fillOval(x + 10, y + 5, 10, 10);
			g.drawLine(x +15, y + 10, x , y + 10);
			break;
		case 3:
			//right
			g.fill3DRect(x, y, 30, 5, false);
			g.fill3DRect(x, y + 15, 30, 5, false);
			g.fill3DRect(x + 5 , y + 5, 20, 10, false);
			g.fillOval(x + 10, y + 5, 10, 10);
			g.drawLine(x +15, y + 10, x + 30, y + 10);
			break;
			
		}
	}
	//������ʾ��Ϣ����
	public void showInfo(Graphics g)
	{
		this.drawTank(80, 330, g, 0, 1);
		g.setColor(Color.BLACK);
		g.drawString(Recorder.getEneNum()+"",110 ,350);
		//����̹�ˣ�֮���װ��
		this.drawTank(140, 330, g, 0, 0);
		g.setColor(Color.BLACK);
		g.drawString(Recorder.getMyNum()+"",170 ,350);
		//��������ܳɼ�
		this.drawTank(420, 60, g, 0, 1);
		g.setColor(Color.BLACK);
		Font f = new Font("����" , Font.BOLD , 20);
		g.setFont(f);
		g.drawString("�����ܳɼ�", 420, 30);
		g.drawString(Recorder.getAllEneNum() +"" , 450 , 80);
	}
	//�ж��Ƿ���ез�̹��
	public void hitEnemyTank()
	{
		for (int i = 0; i < hero.ss.size(); i++)
		{
			Shot myShot = hero.ss.get(i);
			if (myShot.isLive == true)
			{
				for(int j = 0 ; j < ets.size(); j++)
				{
					EnemyTank et = ets.get(j);
					if(et.isLive)
					{
						this.hitTank(myShot, et);
						
					}
				}
			}
		}
	}

	//�жϵз�̹���Ƿ�����ҷ�̹��
	public void hitMe()
	{
		for(int i=0; i<ets.size() ;i ++)
		{
			EnemyTank et = ets.get(i);
				for (int j = 0; j< et.ss.size(); j ++)
				{
					Shot s = et.ss.get(j);
					if(s.isLive)
					{
						if(hero.isLive)
						{
						this.hitTank(s, hero);
						}
					}
				}
		}
	}
	public void hitTank(Shot s , Tank et)
	{
		EnemyTank t = new EnemyTank();
		Hero h = new Hero();
		switch(et.direct)
		{
		case 0:
		case 1:
			if(s.x > et.x && s.x < et.x + 20 && s.y > et.y && s.y< et.y+30)
			{
				//����
				s.isLive = false;
				et.isLive = false;
				Bomb b = new Bomb(et.x , et.y);
				bombs.add(b);
				if(et instanceof EnemyTank)
				{
					Recorder.reduceEnemy();
					Recorder.addEneNum();
				}
				else
				{
					Recorder.reduceHero();
				}
			}
			break;
		case 2:
		case 3:	
			if(s.x > et.x && s.x < et.x + 30 && s.y > et.y && s.y < et.y + 20)
			{
				//����
				s.isLive = false;
				et.isLive = false;
				Bomb b = new Bomb(et.x , et.y);
				bombs.add(b);
				if(et instanceof EnemyTank)
				{
					Recorder.reduceEnemy();
					Recorder.addEneNum();
				}
				else
				{
					Recorder.reduceHero();
				}
			break;
			}
		}
	}
//w up; a left ; s down ;d right;
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			this.hero.setDirect(0);
			this.hero.moveUp();
		}
		else if(e.getKeyCode() == KeyEvent.VK_S)
		{
			this.hero.setDirect(1);
			this.hero.moveDown();
		}
		else if(e.getKeyCode() == KeyEvent.VK_A)
		{
			this.hero.setDirect(2);
			this.hero.moveLeft();
		}
		else if(e.getKeyCode() == KeyEvent.VK_D)
		{
			this.hero.setDirect(3);
			this.hero.moveRight();
		}
		if (e.getKeyCode() == KeyEvent.VK_J)
		{
			//����j�������ӵ��������ܷ���
			if(this.hero.ss.size() <= 4 && hero.isLive)
			{
				this.hero.shotEnermy();
			}
		}

		this.repaint();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.hitEnemyTank();
		this.hitMe();
		this.repaint();
	}
	}
}

//������ʼ���
class MyStartPanel extends JPanel implements Runnable
{
	int times = 0;
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		if(times % 2 == 0)
		{
			g.setColor(Color.cyan);
			g.setFont(new Font("TimesRomam" , Font.BOLD , 40));
			g.drawString("State 1" , 150 , 150);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		times++;
		this.repaint();
		}
	}
}

class Recorder
{
	private static int myNum = 3;
	private static int eneNum = 20;
	private static int allEneNum = 0;
	
	public static int getAllEneNum() {
		return allEneNum;
	}
	public static void setAllEneNum(int allEneNum) {
		Recorder.allEneNum = allEneNum;
	}
	public static int getMyNum() {
		return myNum;
	}
	public static void setMyNum(int myNum) {
		Recorder.myNum = myNum;
	}
	public static int getEneNum() {
		return eneNum;
	}
	public static void setEneNum(int entNum) {
		Recorder.eneNum = entNum;
	}
	//����һ������
	public static void reduceEnemy()
	{
		eneNum--;
	}
	//����һ���ҷ�̹��
	public static void reduceHero()
	{
		myNum --;
	}
	public static void addEneNum()
	{
		allEneNum++ ;
	}
	//���ܳɼ�����
	private FileWriter fw = null;
	private BufferedWriter bw = null;
	public FileReader fr = null;
	public BufferedReader br = null;
	private static Vector<EnemyTank> ets = new Vector<EnemyTank>();
	static Vector<Node> nodes = new Vector<Node>();
	private String n;
	public static Vector<EnemyTank> getEts() {
		return ets;
	}
	public static void setEts(Vector<EnemyTank> ets) {
		Recorder.ets = ets;
	}
	public void keepRecording()
	{
		try {
			fw = new FileWriter("d:\\myrecording.txt");
			bw = new BufferedWriter(fw);
			
			bw.write(allEneNum +"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void getRecording() throws IOException
	{
		try {
			fr = new FileReader("d:\\myrecording.txt");
			br = new BufferedReader(fr);
			String n = br.readLine();
			allEneNum = Integer.parseInt(n);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				br.close();
				fr.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	//��ȡԭ���Ĵ浵
	public Vector<Node> getEneAndRecording() throws IOException
	{
		try {
			fr = new FileReader("d:\\myrecording.txt");
			br = new BufferedReader(fr);
			String n1 = "";
			n1 = br.readLine();
			System.out.println(n1);
			allEneNum = Integer.parseInt(n1);
			while ((n1 = br.readLine())!= null)
			{
				System.out.println(br.readLine());
				System.out.println(n1);
				String xyz[] = n1.split(" ");
				Node node = new Node(Integer.parseInt(xyz[0]) , Integer.parseInt(xyz[1]) , Integer.parseInt(xyz[2]));
				nodes.add(node);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				br.close();
				fr.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return nodes;
	}
	
	public void keepRecAndEnemyTank()
	{

		try {
			fw = new FileWriter("d:\\myrecording.txt");
			bw = new BufferedWriter(fw);
			bw.write(allEneNum +"\r\n");
			for (int i = 0 ; i < ets.size() ; i ++)
			{
				EnemyTank et = ets.get(i);
				String recorder = et.x + " " + et.y + " " + et.direct ;
				bw.write(recorder + "\r\n");
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Node
{
	int x;
	int y;
	int direct;
	public Node(int x , int y , int direct)
	{
		this.x = x;
		this.y = y;
		this.direct = direct;
	}
}