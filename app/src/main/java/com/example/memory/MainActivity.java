package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timer;
    private TextView errors;
    private Button resetBtn;
    ImageButton[] buttonsShowed = new ImageButton[2];

    private int seconds = 0;
    private int minutes = 0;
    private int errorCounter = 0;
    private int pairsFound = 0;

    //Integer[] images = {R.drawable.memory};

    private int buttonsSelected = 0;

    List imageNum = new ArrayList();

    ButtonImage[] buttonImage = new ButtonImage[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = findViewById(R.id.textViewTime);
        errors = findViewById(R.id.textViewErrors);
        resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        for(int i = 0; i < 15; i++) {
            imageNum.add(i);
            imageNum.add(i);
        }

        newGame();

        /*Collections.shuffle(imageNum);

        for(int i = 0; i < 30; i++) {
            String buttonID = "btn" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

            int num = (int) imageNum.get(i);
            buttonImage[i] = new ButtonImage(i, (ImageButton) findViewById(resID), num);
            buttonImage[i].getBtn().setOnClickListener(this);

            int drawID = getResources().getIdentifier("memory", "drawable", this.getPackageName());

            buttonImage[i].getBtn().setBackgroundResource(drawID);
        }*/

        Thread t = new Thread() {
            @Override
            public void run() {
                while(!isInterrupted()) {
                    try {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                seconds += 1;

                                if(seconds == 60) {
                                    minutes += 1;
                                    seconds = 0;
                                }
                                timer.setText("Time: "+minutes+"min "+seconds+"s");

                                /*if(minutes == 60) {
                                    t.stop();
                                }*/
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();

        Thread t2 = new Thread() {
            @Override
            public void run() {
                while(!isInterrupted()) {
                    try {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if(buttonsSelected == 2) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    hideImages();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t2.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("seconds", seconds);
        outState.putInt("minutes", minutes);
        outState.putInt("errorCounter", errorCounter);
    }

    //Variablen werden wiederhergestellt
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        seconds = savedInstanceState.getInt("seconds");
        minutes = savedInstanceState.getInt("minutes");
        errorCounter = savedInstanceState.getInt("errorCounter");
    }

    @Override
    public void onClick(View view) {

        if(buttonsSelected < 2) {
            showImages((ImageButton) view);
            buttonsShowed[buttonsSelected] = (ImageButton) view;

            buttonsSelected += 1;
        }

    }

    public void showImages(ImageButton btn) {
        for(ButtonImage bI: buttonImage) {
            if(btn.getId() == bI.getBtn().getId()) {
                int imgID = bI.getImageID();

                int drawID = getResources().getIdentifier("btn"+imgID, "drawable", this.getPackageName());

                btn.setBackgroundResource(drawID);

                break;
            }
        }
    }

    public boolean testSimilarity() {
        int imgID1 = -1;
        int imgID2 = -1;

        for(ButtonImage bI: buttonImage) {
            if(buttonsShowed[0].equals(bI.getBtn())) {
                imgID1 = bI.getImageID();
            }
            if(buttonsShowed[1].equals(bI.getBtn())) {
                imgID2 = bI.getImageID();
            }
        }

        if(imgID1 == imgID2 && imgID1 != -1) {
            pairsFound += 1;
            return true;
        }

        return false;
    }

    public void hideImages() {
        if(testSimilarity()) {
            buttonsShowed[0].setVisibility(View.INVISIBLE);
            buttonsShowed[1].setVisibility(View.INVISIBLE);
            if(checkWin()) {
                Toast.makeText(this, "You did it!", Toast.LENGTH_SHORT).show();
                newGame();
            }
        } else {
            int drawID = getResources().getIdentifier("memory", "drawable", this.getPackageName());
            buttonsShowed[0].setBackgroundResource(drawID);
            buttonsShowed[1].setBackgroundResource(drawID);
            errorCounter += 1;
            errors.setText("Errors: "+errorCounter);
        }
        buttonsSelected = 0;
    }

    public boolean checkWin() {
        if(pairsFound == 15) {
            return true;
        }
        return false;
    }

    public void newGame() {
        seconds = 0;
        minutes = 0;
        errorCounter = 0;
        pairsFound = 0;
        buttonsSelected = 0;

        Collections.shuffle(imageNum);

        for(int i = 0; i < 30; i++) {
            String buttonID = "btn" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

            int num = (int) imageNum.get(i);
            buttonImage[i] = new ButtonImage(i, (ImageButton) findViewById(resID), num);
            buttonImage[i].getBtn().setOnClickListener(this);
            buttonImage[i].getBtn().setVisibility(View.VISIBLE);

            int drawID = getResources().getIdentifier("memory", "drawable", this.getPackageName());

            buttonImage[i].getBtn().setBackgroundResource(drawID);
        }
    }

    /*public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getButtonsSelected() {
        return buttonsSelected;
    }

    public TextView getTimer() {
        return timer;
    }

    public TextView getErrors() {
        return errors;
    }*/

    /*@Override
    public void onStop(){
        super.onStop();
        finish();
    }*/

    /*@Override
    protected void onPause() {
        super.onPause();
    }*/
    /*@Override
    protected void onRestart() {
        super.onRestart();

        System.out.println("Hi");
    }*/
}

/*for(int i = 0; i < 30; i++) {
            imageNum.add(i);
        }

        Collections.shuffle(imageNum);

        for(int i = 0; i < 30; i++) {
            String buttonID = "btn" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            //buttons[i] = findViewById(resID);
            //buttons[i].setOnClickListener(this);

            int num = (int) imageNum.get(i);
            buttonImage[i] = new ButtonImage(i, (ImageButton) findViewById(resID), num);
            buttonImage[i].getBtn().setOnClickListener(this);

            int drawID = getResources().getIdentifier("memory", "drawable", this.getPackageName());

            buttonImage[i].getBtn().setBackgroundResource(drawID);

            //buttons[i].setBackgroundResource(drawID);
        }*/