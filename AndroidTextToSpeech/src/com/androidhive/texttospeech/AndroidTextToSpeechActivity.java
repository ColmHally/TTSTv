package com.androidhive.texttospeech;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.*;
import java.io.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AndroidTextToSpeechActivity extends Activity implements
		TextToSpeech.OnInitListener {
	/** Called when the activity is first created. */

	private TextToSpeech tts;
	private Button btnSpeak;
	private EditText txtText;
	private EditText IpText;
	private EditText ApiTag;
	
	private static NetworkXMLOperationsQueue operationsQueue;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tts = new TextToSpeech(this, this);

		btnSpeak = (Button) findViewById(R.id.btnSpeak);

		txtText = (EditText) findViewById(R.id.txtText);
		
		IpText = (EditText) findViewById(R.id.IpText);
		
		ApiTag = (EditText) findViewById(R.id.ApiTag);

		// button on click event
		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					speakOut();
					performNetworkOp();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		operationsQueue = new NetworkXMLOperationsQueue();
		new Thread( operationsQueue ).start();
	}

	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			// tts.setPitch(5); // set pitch level

			// tts.setSpeechRate(2); // set speech speed rate

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
				btnSpeak.setEnabled(true);
				try {
					speakOut();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			

		} else {
			Log.e("TTS", "Initilization Failed");
		}

	}

	private void speakOut() throws IOException {

		String text = txtText.getText().toString();		
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	private void performNetworkOp() throws IOException {
		String IP = IpText.getText().toString();
		String Tag = ApiTag.getText().toString();
		operationsQueue.addOperation( getDreamboxURL( IP, Tag ), new Callback() {
			
			public void run( Response response ) {
				if ( response.error != null )
					System.err.println( "\n\n" + response );
				
				else
					System.out.println( "\n\n" + response );
			}
			
		} );
		
	}
	
	public static String getDreamboxURL( String ip, String endpoint ) {
		if ( ip.length() == 0 )
			return null;
		
		return "http://" + ip + "/web/" + endpoint;
	}
	
}