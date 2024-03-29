package com.dev.andy.dR;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	GameLoopThread gameLoopThread;
	SurfaceHolder holder;
	
	public static int globalxSpeed = 8;
	
	Bitmap playerbmp;
	Bitmap coinbmp;
	Bitmap groundbmp;
	Bitmap spikesbmp;
	Bitmap powerupshieldbmp;
	Bitmap buttonsbmp;
	int xx = 0;
	
	private List<Coin> coins = new ArrayList<Coin>();
	private List<Player> player = new ArrayList<Player>();
	private List<Ground> ground = new ArrayList<Ground>();
	private List<Spikes> spikes = new ArrayList<Spikes>();
	private List<PowerupShield> powerupshield = new ArrayList<PowerupShield>();
	private List<Buttons> buttons = new ArrayList<Buttons>();
	
	public static int Coinscollected = 0;
	public static int Score = 0;
	public static int HighScore = 0;

	
	private boolean PlayerGotPowerupShield = false;
	
	private int PlayerPowerupShieldTimer = 120;
	
	private static SharedPreferences prefs;
	
	private int timerCoins = 0;
	private int timerSpikes = 0;
	private int timerPowerupShield = 0;
	
	private int timerRandomPowerupShield = 0;
	private int timerRandomSpikes = 1;
	
	private int lastScore = 0;
	

	private String saveScore = "Highscore";
	private String Menu = "Running";
	
	

	
	public GameView(Context context) {
		super(context);
		prefs = context.getSharedPreferences("com.dev.andy.dR",context.MODE_PRIVATE);
		
		String spackage ="com.dev.andy.dR";
		
		HighScore = prefs.getInt(saveScore , 0);

		
		gameLoopThread = new GameLoopThread(this);
		
		holder = getHolder();
		holder.addCallback(new Callback() {
			
			public void surfaceDestroyed(SurfaceHolder arg0) {
				// TODO Auto-generated method stub
				Score =0;
				Coinscollected = 0;
				prefs.edit().putInt(saveScore,HighScore).commit();
		
				gameLoopThread.running = false;		
			}
			
			public void surfaceCreated(SurfaceHolder arg0) {
				// TODO Auto-generated method stub
				gameLoopThread.setRunning();
				gameLoopThread.start();
				
				
			}
		
			public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub			
				
			}
			
		});
		playerbmp = BitmapFactory.decodeResource(getResources(), R.drawable.player);
		coinbmp = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
		groundbmp = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
		spikesbmp = BitmapFactory.decodeResource(getResources(), R.drawable.spikes);
		powerupshieldbmp = BitmapFactory.decodeResource(getResources(), R.drawable.powerupshield);
		buttonsbmp = BitmapFactory.decodeResource(getResources(), R.drawable.buttons);
		
		powerupshield.add(new PowerupShield(this,powerupshieldbmp,600,32));
		spikes.add(new Spikes(this,spikesbmp,400,0));
		spikes.add(new Spikes(this,spikesbmp,800,0));
		player.add(new Player(this,playerbmp,50,50));
		coins.add(new Coin(this,coinbmp,120,32));

		coins.add(new Coin(this,coinbmp,50,0));
		// TODO Auto-generated constructor stub
	}
	

@Override
public boolean onTouchEvent(MotionEvent e){
	for(Player pplayer: player)
	{
		pplayer.ontouch();
	}
	if (Menu =="Mainmenu")
	{
		for(int i = 0; i < buttons.size(); i++){
			if (buttons.get(i).getState() == 1){   
				if ((buttons.get(i).getX()<e.getX() && buttons.get(i).getX()+84>e.getX())){
			if (buttons.get(i).getY()<e.getY() && buttons.get(i).getY()+32>e.getY()){
				Menu = "Running";
				startGame();}	
			}
		}
		
		}
	}
	return false;
	
}


public void update(){
	if(Menu=="Running"){
	Score += 5;
	lastScore = Score;
	updatetimers();
	deleteground();

	if (Score > HighScore)
	{
		HighScore = Score;
	}}
}

public void updatetimers(){
	
	timerCoins ++;
	timerSpikes ++;
	timerPowerupShield ++;
	if (Menu =="Running"){
	if (PlayerGotPowerupShield){
		PlayerPowerupShieldTimer --;
		if (PlayerPowerupShieldTimer <= 0)
		{
			PlayerGotPowerupShield = false;
		}
	}
	
	switch(timerRandomPowerupShield){
	
	case 0:
		if(timerPowerupShield >= 150){
			powerupshield.add(new PowerupShield(this,powerupshieldbmp,this.getWidth()+32,0));
			Random randomPowerupShield = new Random();
			timerRandomPowerupShield = randomPowerupShield.nextInt(3);
			timerPowerupShield = 0;
			
		}break;
	case 1:
		if(timerPowerupShield >= 250){
			powerupshield.add(new PowerupShield(this,powerupshieldbmp,this.getWidth()+32,0));
			Random randomPowerupShield = new Random();
			timerRandomPowerupShield = randomPowerupShield.nextInt(3);
			timerPowerupShield = 0;
			
		}break;
	case 2:
		if(timerPowerupShield >= 350){
			powerupshield.add(new PowerupShield(this,powerupshieldbmp,this.getWidth()+32,0));
			Random randomPowerupShield = new Random();
			timerRandomPowerupShield = randomPowerupShield.nextInt(3);
			timerPowerupShield = 0;
			
		}break;
	}
	switch(timerRandomSpikes){
	
	case 0:
	if(timerSpikes >= 125)
	{
		spikes.add(new Spikes(this,spikesbmp,this.getWidth()+24,0));
		Random randomSpikes = new Random();
		timerRandomSpikes = randomSpikes.nextInt(3);
		timerSpikes = 0;
	}break;
	case 1:
		if(timerSpikes >= 175)
		{
			spikes.add(new Spikes(this,spikesbmp,this.getWidth()+24,0));
			Random randomSpikes = new Random();
			timerRandomSpikes = randomSpikes.nextInt(3);
			timerSpikes = 0;
		}break;
		
	case 2:
		if(timerSpikes >= 100)
		{

			spikes.add(new Spikes(this,spikesbmp,this.getWidth()+24,0));
			Random randomSpikes = new Random();
			timerRandomSpikes = randomSpikes.nextInt(3);
			timerSpikes = 0;
		}
		break;
	}
	
	if (timerCoins >= 100){
		
		Random randomCoin = new Random();
		int random;
		random = randomCoin.nextInt(3);
		
		switch(random){
		
		case 1:
			int currentcoin = 1;
			int xx = 1;
			while(currentcoin <= 5){
				
				coins.add(new Coin(this,coinbmp,this.getWidth()+(32*xx),32));
				
				currentcoin++;
				xx++;
			}
			break;
			
		case 2:
			currentcoin = 1;

			coins.add(new Coin(this,coinbmp,this.getWidth()+32,32));
			coins.add(new Coin(this,coinbmp,this.getWidth()+64,48));
			coins.add(new Coin(this,coinbmp,this.getWidth()+96,32));
			coins.add(new Coin(this,coinbmp,this.getWidth()+128,48));
			coins.add(new Coin(this,coinbmp,this.getWidth()+160,32));
			
		}
		timerCoins = 0;
	}
	}
}


public void addground(){
	
	while(xx < this.getWidth()+Ground.width)
	{
		ground.add(new Ground(this,groundbmp,xx,0));
		
		xx += groundbmp.getWidth();
	}
	
}

public void deleteground(){
		
	for (int i = ground.size()-1;i >= 0; i--)
	{
		int groundx = ground.get(i).returnX();
		
		if (groundx<=-Ground.width){
			ground.remove(i);
			ground.add(new Ground(this,groundbmp,groundx+this.getWidth()+Ground.width,0));
		}
	}
	
}
public void startGame(){
	for(int i = 0; i < buttons.size(); i++){
		buttons.remove(i);
	}
	player.add(new Player(this,playerbmp,50,50));

}

public void endGame(){
	Menu  = "Mainmenu";
	timerCoins =0;
	timerSpikes =0;
	timerPowerupShield=0;
	buttons.add(new Buttons(this,buttonsbmp,this.getWidth()/2-64,this.getHeight()/2,3));
	buttons.add(new Buttons(this,buttonsbmp,this.getWidth()/2-64,this.getHeight()/2+48,1));
	for(int i = 0; i < coins.size(); i++)
	{
		coins.remove(i);
		}
	for(int i = 0; i < spikes.size(); i++)
	{
		spikes.remove(i);
		}
	for(int i = 0; i < powerupshield.size(); i++)
	{
		powerupshield.remove(i);
		}
	player.remove(0);
	
}

@Override
protected void onDraw(Canvas canvas) {
	
	update();
	canvas.drawColor(Color.CYAN);

	if (Menu=="Mainmenu")
	{
		for(Buttons bbuttons: buttons)
		{
			bbuttons.onDraw(canvas);
		
	}
	
	}
	// If the game is running, draw it
	if (Menu == "Running"){
	addground();
	Paint textpaint = new Paint();
	
	textpaint.setTextSize(32);
	
	canvas.drawText("Score: "+String.valueOf(Score), 0, 32, textpaint);
	canvas.drawText("High Score: "+String.valueOf(HighScore), 0, 64, textpaint);
	canvas.drawText("Coins"+String.valueOf(Coinscollected), 0, 96, textpaint);

	
	for(Ground gground: ground){
		gground.onDraw(canvas);
	}

	for(Player pplayer: player)
	{
		pplayer.onDraw(canvas);
	}
	for(int i = 0; i < spikes.size(); i++)
	{
		
		spikes.get(i).onDraw(canvas);
		Rect playerr = player.get(0).GetBounds();
		Rect spikesr = spikes.get(i).GetBounds();
		
	
		
		if (spikes.get(i).checkCollision(playerr, spikesr)){
			if(!PlayerGotPowerupShield){
			
			Score = 0;
			Coinscollected=0;
			endGame();}
			else{
				spikes.remove(i);
				PlayerGotPowerupShield = false;
			}
			break;
		}
		
		
	}
	for(int i = 0; i < coins.size(); i++)
	{
		
		coins.get(i).onDraw(canvas);
		Rect playerr = player.get(0).GetBounds();
		Rect coinr = coins.get(i).GetBounds();
		
		if (coins.get(i).returnX() < 0-32){
			coins.remove(i);
			
		}
		else if (coins.get(i).checkCollision(playerr, coinr)){
			coins.remove(i);
			Score += 500;
			Coinscollected+=1;
			
		}
	}
		for(int i = 0; i < powerupshield.size(); i++)
		{
			
			powerupshield.get(i).onDraw(canvas);
			Rect playerr = player.get(0).GetBounds();
			Rect powerupshieldr = powerupshield.get(i).GetBounds();
			
			if (powerupshield.get(i).returnX() < 0-32){
				powerupshield.remove(i);
				
			}
			else if(powerupshield.get(i).checkCollision(playerr, powerupshieldr)){
				powerupshield.remove(i);
				PlayerGotPowerupShield = true;
				PlayerPowerupShieldTimer = 120;
				
			}
		
		
		
		
	}
	
	}
	if (Menu=="Mainmenu")
		{Paint textpaint = new Paint();
		
		textpaint.setTextSize(32);
		
		canvas.drawText("Score: "+String.valueOf(lastScore), canvas.getWidth()/2, canvas.getHeight()/2, textpaint);
		
		}
		}






		
}
