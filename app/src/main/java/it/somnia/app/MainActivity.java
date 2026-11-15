package it.somnia.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
  final int NAVY=0xff111d48, WHITE=0xfff9faff, MUTED=0xffb8c5e6, TEAL=0xff37c9c1, LILAC=0xffaa92ff;
  final String[] titles={"Lingua al palato","Scorrimento sul palato","Pressione delicata"};
  final String[] desc={
    "Appoggia delicatamente la punta della lingua sul palato, subito dietro gli incisivi superiori. Evita di premere sui denti. Mantieni una respirazione tranquilla.",
    "Parti con la punta della lingua dietro gli incisivi superiori. Falla scorrere lentamente verso il fondo del palato, poi ritorna alla posizione iniziale.",
    "Appoggia la lingua al palato ed esercita una pressione molto leggera per tre secondi, poi rilassa. Non forzare e interrompi se avverti dolore."
  };
  TextToSpeech tts; boolean voiceReady=false; String voiceLabel="Voce italiana del dispositivo";
  LinearLayout page; int exercise=0; boolean inExercise=false;
  int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
  @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(NAVY);getWindow().setNavigationBarColor(NAVY);tts=new TextToSpeech(this,this);showHome();}
  GradientDrawable bg(int top,int bottom,int radius){GradientDrawable g=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{top,bottom});g.setCornerRadius(dp(radius));return g;}
  TextView label(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setIncludeFontPadding(true);if(bold)t.setTypeface(android.graphics.Typeface.DEFAULT,1);return t;}
  LinearLayout vertical(){LinearLayout l=new LinearLayout(this);l.setOrientation(1);return l;}
  void gap(LinearLayout l,int h){View v=new View(this);l.addView(v,new LinearLayout.LayoutParams(1,dp(h)));}
  void pad(View v,int a){v.setPadding(dp(a),dp(a),dp(a),dp(a));}
  void base(){ScrollView sc=new ScrollView(this);sc.setFillViewport(true);sc.setBackground(bg(0xff101a43,0xff514ba3,0));page=vertical();page.setPadding(dp(20),dp(22),dp(20),dp(28));sc.addView(page);setContentView(sc);}
  TextView pill(String s,int top,int bottom,int color){TextView t=label(s,16,color,true);t.setGravity(Gravity.CENTER);t.setPadding(dp(14),dp(18),dp(14),dp(18));t.setBackground(bg(top,bottom,22));return t;}
  void add(LinearLayout l,View v){l.addView(v,new LinearLayout.LayoutParams(-1,-2));}
  void header(){TextView flower=label("✦  S O M N I A  ✦",30,WHITE,true);flower.setGravity(Gravity.CENTER);add(page,flower);TextView sub=label("La Palestra del Respiro",16,0xffd9e0ff,false);sub.setGravity(Gravity.CENTER);add(page,sub);gap(page,22);}

  LinearLayout tile(String icon,String title,int start,int end,Runnable action){
    LinearLayout box=vertical();box.setGravity(Gravity.CENTER);box.setPadding(dp(5),dp(12),dp(5),dp(12));box.setBackground(bg(start,end,24));
    TextView symbol=label(icon,30,WHITE,true);symbol.setGravity(Gravity.CENTER);add(box,symbol);gap(box,5);
    TextView name=label(title,16,WHITE,true);name.setGravity(Gravity.CENTER);add(box,name);box.setOnClickListener(v->action.run());return box;
  }
  void pair(String i,String t,int a,int b,Runnable one,String j,String u,int c,int d,Runnable two){
    LinearLayout line=new LinearLayout(this);
    line.addView(tile(i,t,a,b,one),new LinearLayout.LayoutParams(0,dp(120),1));
    View space=new View(this);line.addView(space,new LinearLayout.LayoutParams(dp(12),1));
    line.addView(tile(j,u,c,d,two),new LinearLayout.LayoutParams(0,dp(120),1));add(page,line);gap(page,12);
  }
  void showHome(){
    inExercise=false;base();
    TextView tools=label("♧                                               ⚙",23,WHITE,true);tools.setGravity(Gravity.CENTER);tools.setOnClickListener(v->showSection("Impostazioni"));add(page,tools);gap(page,8);
    add(page,new MoonLogo(this));gap(page,7);
    TextView name=label("SOMNIA",40,WHITE,true);name.setGravity(Gravity.CENTER);add(page,name);
    TextView sub=label("LA PALESTRA DEL RESPIRO",15,WHITE,true);sub.setGravity(Gravity.CENTER);add(page,sub);gap(page,24);
    pair("◎","Il mio percorso",0xffa93ce7,0xff5d25aa,()->showSection("Il mio percorso"),"♧","Esercizi",0xff009ae1,0xff223eae,()->showExercise(0));
    pair("▥","Progressi",0xff05b8c8,0xff155e91,()->showSection("Progressi"),"♢","Promemoria",0xffffb85d,0xffbd663f,()->showSection("Promemoria"));
    pair("▤","Approfondimenti",0xffcf62b3,0xff7c298e,()->showSection("Approfondimenti"),"⚙","Impostazioni",0xff4870d7,0xff40278e,()->showSection("Impostazioni"));
    bottomNavigation();
  }
  void bottomNavigation(){
    gap(page,12);LinearLayout bar=new LinearLayout(this);bar.setBackground(bg(0xff293b85,0xff111a47,23));
    String[] names={"⌂\nHome","▦\nPercorso","▷\nEsercizi","▥\nProgressi","♙\nProfilo"};
    for(int i=0;i<5;i++){final int n=i;TextView t=label(names[i],12,WHITE,false);t.setGravity(Gravity.CENTER);t.setPadding(0,dp(13),0,dp(13));bar.addView(t,new LinearLayout.LayoutParams(0,-2,1));t.setOnClickListener(v->{if(n==0)showHome();else if(n==1)showSection("Il mio percorso");else if(n==2)showExercise(0);else if(n==3)showSection("Progressi");else showSection("Impostazioni");});}
    add(page,bar);
  }
  void showSection(String title){
    inExercise=false;if(tts!=null)tts.stop();base();
    TextView back=label("‹  Torna alla Home",17,MUTED,true);back.setOnClickListener(v->showHome());add(page,back);gap(page,20);
    add(page,label(title,29,WHITE,true));gap(page,18);
    if(title.equals("Il mio percorso")){
      add(page,label("Tre esercizi guidati, da svolgere con calma.",18,WHITE,false));gap(page,16);
      TextView start=pill("▶  Inizia",0xff8250d7,0xff395ec3,WHITE);start.setOnClickListener(v->showExercise(0));add(page,start);
    }else if(title.equals("Progressi")){
      add(page,label("Esercizi completati: "+getPreferences(0).getInt("completed",0),22,TEAL,true));
      gap(page,12);add(page,label("Il conteggio si aggiorna quando registri un esercizio completato.",16,MUTED,false));
    }else if(title.equals("Promemoria")){
      add(page,label("I promemoria automatici non sono ancora disponibili in questa versione.",17,WHITE,false));
    }else if(title.equals("Approfondimenti")){
      add(page,label("Respirazione e benessere",21,TEAL,true));gap(page,10);
      add(page,label("Esegui gli esercizi lentamente, senza forzare. Le indicazioni sono educative e non sostituiscono una valutazione sanitaria.",17,WHITE,false));
    }else{
      add(page,label("Voce guida",21,TEAL,true));gap(page,10);add(page,label(voiceLabel,15,WHITE,false));gap(page,16);
      TextView test=pill("▶  Prova la voce",0xff376cb4,0xff7156bf,WHITE);
      test.setOnClickListener(v->{if(voiceReady)tts.speak("Benvenuto in SOMNIA, la palestra del respiro.",TextToSpeech.QUEUE_FLUSH,null,"test");});add(page,test);
    }
    gap(page,25);bottomNavigation();
  }
  class MoonLogo extends View{
    Paint p=new Paint(3);
    MoonLogo(Context context){super(context);setLayoutParams(new LinearLayout.LayoutParams(-1,dp(190)));}
    @Override protected void onDraw(Canvas c){super.onDraw(c);float w=getWidth(),h=getHeight(),x=w*.49f,y=h*.49f,r=h*.42f;
      p.setShader(new LinearGradient(0,0,w,h,new int[]{0xff111d50,0xff4d2592,0xff111c50},null,Shader.TileMode.CLAMP));c.drawRoundRect(0,0,w,h,dp(24),dp(24),p);p.setShader(null);
      p.setColor(0xffffce74);c.drawCircle(x,y,r,p);p.setColor(0xff23266c);c.drawCircle(x+r*.38f,y-r*.12f,r*.91f,p);
      p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(4));p.setColor(0xff6fd9ff);
      Path face=new Path();face.moveTo(x,y-r*.68f);face.cubicTo(x+r*.33f,y-r*.53f,x+r*.30f,y-r*.23f,x+r*.53f,y-r*.12f);face.cubicTo(x+r*.72f,y,x+r*.48f,y+r*.09f,x+r*.58f,y+r*.19f);face.cubicTo(x+r*.77f,y+r*.39f,x+r*.45f,y+r*.44f,x+r*.30f,y+r*.39f);face.cubicTo(x+r*.10f,y+r*.56f,x+r*.22f,y+r*.73f,x+r*.25f,y+r*.86f);c.drawPath(face,p);p.setStyle(Paint.Style.FILL);
      p.setColor(0xffffe3a1);for(int i=0;i<8;i++)c.drawCircle(w*(.12f+i*.10f),h*(.13f+(i*3%7)*.10f),dp(1.5f),p);
    }
  }
  void showExercise(int n){
    inExercise=true;exercise=n;if(tts!=null)tts.stop();base();
    TextView back=label("‹  Torna alla Home",16,MUTED,true);back.setOnClickListener(v->showHome());add(page,back);gap(page,16);
    header();add(page,label("ESERCIZIO "+(exercise+1)+" DI 3",14,TEAL,true));gap(page,7);
    add(page,label(titles[exercise],26,WHITE,true));gap(page,16);
    LinearLayout illustration=vertical();pad(illustration,12);illustration.setBackground(bg(0xffe8effe,0xfff9f7ff,24));
    add(illustration,new Diagram(this));add(illustration,label("Vista laterale schematica · freccia = direzione della lingua",12,0xff445a78,false));add(page,illustration);
    gap(page,17);
    LinearLayout instructions=vertical();pad(instructions,19);instructions.setBackground(bg(0xff334b85,0xff3e5594,20));
    add(instructions,label("Come eseguire il movimento",19,WHITE,true));gap(instructions,10);add(instructions,label(desc[exercise],17,WHITE,false));add(page,instructions);
    gap(page,14);
    TextView speak=pill("▶  Ascolta la guida",0xff27b4b3,0xff168f9c,WHITE);speak.setOnClickListener(v->speak());add(page,speak);gap(page,9);
    TextView stop=pill("■  Ferma la voce",0xff526297,0xff404e82,WHITE);stop.setOnClickListener(v->{if(tts!=null)tts.stop();});add(page,stop);
    gap(page,9);add(page,label(voiceLabel,12,MUTED,false));gap(page,15);
    LinearLayout nav=new LinearLayout(this);nav.setOrientation(0);
    TextView prev=pill("‹  Indietro",0xff5955a2,0xff6d66b9,WHITE);prev.setOnClickListener(v->showExercise((exercise+2)%3));nav.addView(prev,new LinearLayout.LayoutParams(0,-2,1));
    View space=new View(this);nav.addView(space,new LinearLayout.LayoutParams(dp(10),1));
    TextView next=pill("Avanti  ›",0xff6964cf,0xff8c6bea,WHITE);next.setOnClickListener(v->showExercise((exercise+1)%3));nav.addView(next,new LinearLayout.LayoutParams(0,-2,1));add(page,nav);
    gap(page,12);TextView done=pill("✓  Segna come completato",0xff229d99,0xff2d708a,WHITE);done.setOnClickListener(v->{int count=getPreferences(0).getInt("completed",0)+1;getPreferences(0).edit().putInt("completed",count).apply();showSection("Progressi");});add(page,done);
    gap(page,15);add(page,label("Se senti dolore, interrompi l'esercizio. Evita pressioni forti.",12,MUTED,false));
  }
  void speak(){if(!voiceReady){Toast.makeText(this,"Installa o attiva una voce italiana nelle impostazioni di sintesi vocale Android.",Toast.LENGTH_LONG).show();return;}tts.speak(titles[exercise]+". "+desc[exercise],TextToSpeech.QUEUE_FLUSH,null,"somnia-"+exercise);}
  @Override public void onInit(int status){
    if(status!=TextToSpeech.SUCCESS)return;
    int r=tts.setLanguage(Locale.ITALIAN);voiceReady=r!=TextToSpeech.LANG_MISSING_DATA&&r!=TextToSpeech.LANG_NOT_SUPPORTED;
    if(!voiceReady)return;
    Voice best=null;int score=-9999;
    Set<Voice> voices=tts.getVoices();
    if(voices!=null)for(Voice v:voices){
      if(!"it".equals(v.getLocale().getLanguage()))continue;
      int s=(v.getLocale().getCountry().equalsIgnoreCase("IT")?20:0)+(v.isNetworkConnectionRequired()?-25:15)+(v.getQuality()*4)-(v.getLatency()*2);
      String name=v.getName().toLowerCase(Locale.ROOT);if(name.contains("female")||name.contains("fem"))s+=4;
      if(s>score){score=s;best=v;}
    }
    if(best!=null){tts.setVoice(best);voiceLabel="Voce italiana: "+best.getName();}
    tts.setSpeechRate(0.89f);tts.setPitch(1.0f);
    runOnUiThread(()->{if(inExercise)showExercise(exercise);});
  }
  @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
  class Diagram extends View{
    Paint p=new Paint(3);
    Diagram(Context c){super(c);setLayoutParams(new LinearLayout.LayoutParams(-1,dp(240)));}
    void path(Canvas c,int color,float width,Path path){p.setColor(color);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(width);p.setStrokeCap(Paint.Cap.ROUND);p.setStrokeJoin(Paint.Join.ROUND);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);}
    @Override protected void onDraw(Canvas c){super.onDraw(c);int save=c.save();c.scale(getWidth()/320f,getHeight()/230f);
      Path face=new Path();face.moveTo(45,202);face.cubicTo(23,156,31,72,101,40);face.cubicTo(167,15,239,58,250,113);face.cubicTo(254,135,239,150,222,165);face.lineTo(219,211);path(c,0xff6c86a8,4,face);
      Path palate=new Path();palate.moveTo(84,128);palate.cubicTo(128,99,190,104,227,133);path(c,0xff547ca4,7,palate);
      Path mouth=new Path();mouth.moveTo(83,153);mouth.cubicTo(135,166,188,170,226,151);path(c,0xff96a8bc,4,mouth);
      Path tongue=new Path();tongue.moveTo(93,151);tongue.cubicTo(135,136,179,141,205,153);path(c,0xffe882a0,11,tongue);
      Path arrow=new Path();if(exercise==0){arrow.moveTo(101,151);arrow.lineTo(101,125);}else if(exercise==1){arrow.moveTo(104,123);arrow.quadTo(160,89,218,130);}else{arrow.moveTo(155,151);arrow.lineTo(155,121);}path(c,0xff13a9a3,5,arrow);
      float x=exercise==0?101:exercise==1?218:155;float y=exercise==0?125:exercise==1?130:121;
      p.setColor(0xff13a9a3);Path tip=new Path();tip.moveTo(x,y);tip.lineTo(x-7,y+12);tip.lineTo(x+7,y+12);tip.close();c.drawPath(tip,p);
      p.setTextSize(14);p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD));p.setColor(0xff4a6d96);c.drawText("PALATO",163,86,p);p.setColor(0xffc96385);c.drawText("LINGUA",117,189,p);c.restoreToCount(save);
    }
  }
}
