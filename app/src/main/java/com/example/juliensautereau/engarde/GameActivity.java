package com.example.juliensautereau.engarde;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//TODO A supp

public class GameActivity extends AppCompatActivity {

    /*Canvas c;
    Paint paint = new Paint();

    public class CustomView extends View {

        private Rect rectangle;
        private Paint paint;

        public CustomView(Context context) {
            super(context);
            int x = (300);
            int y = (50);
            int sideLength = 200;

            // create a rectangle that we'll draw later
            rectangle = new Rect(x, y, sideLength, sideLength);

            // create the Paint and set its color
            paint = new Paint();
            paint.setColor(Color.WHITE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLUE);
            canvas.drawRect(rectangle, paint);
        }

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
       // setContentView(new CustomView(this));
    }
}
