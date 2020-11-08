package com.example.snake;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private GameView gameView;
    private Canvas canvas;

    // Frame
    private long lastFrameTime = 0;
    private final int fps = 5;

    // Screen
    private int screenWidth;
    private int screenHeight;

    // Game
    private String playerName;

    private Thread playThread = null;
    private volatile boolean playing = false;
    private volatile boolean muted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load
        sp = getSharedPreferences("Game", Context.MODE_PRIVATE);
        playerName = sp.getString("playerName", null);

        System.out.println("Player: " + playerName);

        gameView = new GameView(this);
        setContentView(gameView);

        resume();
    }

    private void resume() {
        playing = true;
        playThread = new Thread(gameView);
        playThread.start();
    }

    private void end() {
        playing = false;
    }

    private void mute() {
        muted = true;
    }

    private void unmute() {
        muted = false;
    }

    private void update() {

    }

    private void redraw() {
        gameView.postInvalidate(0,0,gameView.getWidth(), gameView.getHeight());
    }

    private class GameView extends SurfaceView implements Runnable{
        private SurfaceHolder holder;

        private GameView(Context context) {
            super(context);
            holder = getHolder();
        }

        @Override
        public void onWindowFocusChanged(boolean hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
            if(hasWindowFocus) {
                screenWidth = gameView.getMeasuredWidth();
                screenHeight = gameView.getMeasuredHeight();
            }
        }

        @Override
        public void run() {
            while(playing) {
                long currentFrameTime = System.currentTimeMillis();
                long elapsedFrameTime = currentFrameTime - lastFrameTime;
                long sleepTime = (long)(1000f / fps - elapsedFrameTime);

                canvas = null;
                try {
                    canvas = holder.lockCanvas();

                    if(canvas == null) {
                        playThread.sleep(1);

                        continue;
                    }
                    else if(sleepTime > 0) {
                        playThread.sleep(sleepTime);

                        synchronized(holder) {
                            update();
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    if(canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                        redraw();
                        lastFrameTime = System.currentTimeMillis();
                    }
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int x, y, w, h;

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);

            // Background
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);
        }
    }
}