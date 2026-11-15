package it.somnia.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.*;
import android.widget.*;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    private final String[] titles={"Lingua al palato", "Scorrimento sul palato", "Pressione delicata"};
    private final String[] descriptions={
        "Appoggia delicatamente la punta della lingua sulla zona del palato subito dietro gli incisivi superiori. Non premere sui denti. Respira normalmente.",
        "Parti con la punta della lingua dietro gli incisivi superiori e falla scorrere lentamente all'indietro lungo il palato. Torna alla posizione iniziale.",
        "Appoggia la lingua al palato ed esercita una pressione leggera per tre secondi, poi rilassa. Evita qualsiasi dolore o sforzo eccessivo."};
    private int index=0;
    private TextToSpeech speech;
    private TextView heading, description, counter;
    private Diagram diagram;
    private boolean ready=false;
    @Override public void onCreate(Bundle b){super.onCreate(b); speech=new TextToSpeech(this,this); build();}
    private TextView text(String value,int sp,int color){TextView t=new TextView(this);t.setText(value);t.setTextSize(sp);t.setTextColor(color);t.setPadding(0,12,0,12);return t;}
    private void build(){
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(24,26,24,18);root.setBackgroundColor(Color.rgb(247,248,251));
        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);setContentView(scroll);scroll.addView(root);
        TextView logo=text("SOMNIA",30,Color.rgb(37,57,93));logo.setTypeface(null,1);root.addView(logo);
        root.addView(text("LA PALESTRA DEL SONNO · GUIDA AGLI ESERCIZI",12,Color.rgb(80,96,119)));
        counter=text("",14,Color.rgb(76,96,125));root.addView(counter);
        heading=text("",24,Color.rgb(31,51,78));heading.setTypeface(null,1);root.addView(heading);
        diagram=new Diagram(this);root.addView(diagram,new LinearLayout.LayoutParams(-1,360));
        root.addView(text("SCHEMA LATERALE · DIREZIONE DELLA LINGUA",12,Color.rgb(82,100,122)));
        description=text("",18,Color.rgb(41,51,66));root.addView(description);
        Button listen=new Button(this);listen.setText("▶ Ascolta la guida");listen.setOnClickListener(v->speak());root.addView(listen);
        Button stop=new Button(this);stop.setText("■ Ferma la voce");stop.setOnClickListener(v->{if(speech!=null)speech.stop();});root.addView(stop);
        LinearLayout nav=new LinearLayout(this);nav.setOrientation(LinearLayout.HORIZONTAL);
        Button prev=new Button(this);prev.setText("◀ Indietro");prev.setOnClickListener(v->{index=(index+titles.length-1)%titles.length;update();});nav.addView(prev,new LinearLayout.LayoutParams(0,-2,1));
        Button next=new Button(this);next.setText("Avanti ▶");next.setOnClickListener(v->{index=(index+1)%titles.length;update();});nav.addView(next,new LinearLayout.LayoutParams(0,-2,1));root.addView(nav);
        root.addView(text("Indicazioni educative, non sostituiscono la valutazione di un professionista sanitario. Interrompi se avverti dolore.",12,Color.rgb(105,110,119)));
        update();
    }
    private void update(){counter.setText("ESERCIZIO "+(index+1)+" DI "+titles.length);heading.setText(titles[index]);description.setText(descriptions[index]);diagram.invalidate();if(speech!=null)speech.stop();}
    private void speak(){if(speech!=null&&ready){speech.speak(titles[index]+". "+descriptions[index],TextToSpeech.QUEUE_FLUSH,null,"somnia-exercise");}else{Toast.makeText(this,"Voce italiana non ancora disponibile sul dispositivo",Toast.LENGTH_LONG).show();}}
    @Override public void onInit(int status){if(status==TextToSpeech.SUCCESS){int r=speech.setLanguage(Locale.ITALIAN);ready=r!=TextToSpeech.LANG_MISSING_DATA&&r!=TextToSpeech.LANG_NOT_SUPPORTED;speech.setSpeechRate(0.84f);speech.setPitch(1.04f);}}
    @Override protected void onDestroy(){if(speech!=null){speech.stop();speech.shutdown();}super.onDestroy();}
    class Diagram extends View {
        Paint p=new Paint(3);
        Diagram(Context c){super(c);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        void stroke(Canvas c,int color,float width,Path path){p.setColor(color);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(width);p.setStrokeCap(Paint.Cap.ROUND);p.setStrokeJoin(Paint.Join.ROUND);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);}
        @Override protected void onDraw(Canvas real){super.onDraw(real);Canvas c=real;int save=c.save();c.scale(getWidth()/340f,getHeight()/330f);
            p.setColor(Color.WHITE);c.drawRoundRect(0,0,340,330,24,24,p);
            p.setColor(0xffedf3fa);c.drawRoundRect(14,18,326,308,18,18,p);
            Path head=new Path();head.moveTo(62,260);head.cubicTo(35,215,41,95,120,62);head.cubicTo(184,32,252,66,269,123);head.cubicTo(276,157,253,181,236,195);head.lineTo(234,254);stroke(c,0xff6b7f98,5,head);
            Path palate=new Path();palate.moveTo(101,159);palate.cubicTo(145,125,203,128,244,158);stroke(c,0xff405f83,8,palate);
            Path mouth=new Path();mouth.moveTo(96,188);mouth.cubicTo(143,204,201,202,242,185);stroke(c,0xff8997a8,5,mouth);
            Path tongue=new Path();tongue.moveTo(111,189);tongue.cubicTo(146,171,195,175,220,189);stroke(c,0xffe27f91,12,tongue);
            p.setColor(0xffd0d9e5);c.drawRoundRect(92,161,101,183,3,3,p);
            Path arrow=new Path();if(index==0){arrow.moveTo(115,183);arrow.lineTo(115,151);}else if(index==1){arrow.moveTo(117,151);arrow.quadTo(165,115,222,153);}else{arrow.moveTo(166,184);arrow.lineTo(166,142);}stroke(c,0xff168a91,6,arrow);
            float x=index==0?115:index==1?222:166;float y=index==0?151:index==1?153:142;
            p.setColor(0xff168a91);Path tip=new Path();tip.moveTo(x,y);tip.lineTo(x-8,y+13);tip.lineTo(x+8,y+13);tip.close();c.drawPath(tip,p);
            p.setColor(0xff405f83);p.setTextSize(13);p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD));c.drawText("PALATO",176,112,p);p.setColor(0xffcf6a7c);c.drawText("LINGUA",143,224,p);
            p.setColor(0xff168a91);c.drawText(index==1?"SCORRIMENTO":"MOVIMENTO",105,283,p);c.restoreToCount(save);
        }
    }
}
