package com.annaginagili.translatorapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.annaginagili.translatorapp.databinding.FragmentHomeBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.Locale


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var drawer: DrawerLayout
    lateinit var toggle: ImageView
    lateinit var input: EditText
    lateinit var output: TextView
    lateinit var translate: Button
    lateinit var speech: ImageView
    private val recordAudioRequestCode = 1
    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var voice: ImageView
    private lateinit var textToSpeech: TextToSpeech
    private val args by navArgs<HomeFragmentArgs>()
    lateinit var lang1: Spinner
    lateinit var lang2: Spinner
    private var selectedLang = TranslateLanguage.ENGLISH
    private var targetLang = TranslateLanguage.SPANISH
    lateinit var swap: ImageView
    private var selected = 0
    private var target = 6

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        drawer = binding.drawer
        toggle = binding.toggle
        input = binding.input
        output = binding.output
        translate = binding.translate
        speech = binding.speech
        voice = binding.voice
        lang1 = binding.lang1
        lang2 = binding.lang2
        swap = binding.swap

        (requireActivity() as MainActivity).bottom.visibility = View.VISIBLE

        toggle.setOnClickListener {
            drawer.open()
        }

        val languageList = arrayListOf(LanguageItem(R.drawable.america, "English"),
            LanguageItem(R.drawable.france, "French"), LanguageItem(R.drawable.germany, "German"),
            LanguageItem(R.drawable.italy, "Italian"), LanguageItem(R.drawable.portugal, "Portugal"),
            LanguageItem(R.drawable.polland, "Polish"), LanguageItem(R.drawable.spain, "Spanish"),
            LanguageItem(R.drawable.turkey, "Turkish"))

        val adapter = LanguageAdapter(requireContext(), languageList)
        lang1.adapter = adapter
        lang2.adapter = adapter
        lang2.setSelection(6)

        var options = TranslatorOptions.Builder()
            .setSourceLanguage(selectedLang)
            .setTargetLanguage(targetLang)
            .build()
        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        var translator = Translation.getClient(options)
        lifecycle.addObserver(translator)

        lang1.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2) {
                    0 -> selectedLang = TranslateLanguage.ENGLISH
                    1 -> selectedLang = TranslateLanguage.FRENCH
                    2 -> selectedLang = TranslateLanguage.GERMAN
                    3 -> selectedLang = TranslateLanguage.ITALIAN
                    4 -> selectedLang = TranslateLanguage.PORTUGUESE
                    5 -> selectedLang = TranslateLanguage.POLISH
                    6 -> selectedLang = TranslateLanguage.SPANISH
                    7 -> selectedLang = TranslateLanguage.TURKISH
                }

                options = TranslatorOptions.Builder()
                    .setSourceLanguage(selectedLang)
                    .setTargetLanguage(targetLang)
                    .build()
                translator = Translation.getClient(options)

                translate.setOnClickListener {
                    translate(translator, conditions)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


        lang2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2) {
                    0 -> targetLang = TranslateLanguage.ENGLISH
                    1 -> targetLang = TranslateLanguage.FRENCH
                    2 -> targetLang = TranslateLanguage.GERMAN
                    3 -> targetLang = TranslateLanguage.ITALIAN
                    4 -> targetLang = TranslateLanguage.PORTUGUESE
                    5 -> targetLang = TranslateLanguage.POLISH
                    6 -> targetLang = TranslateLanguage.SPANISH
                    7 -> targetLang = TranslateLanguage.TURKISH
                }

                options = TranslatorOptions.Builder()
                    .setSourceLanguage(selectedLang)
                    .setTargetLanguage(targetLang)
                    .build()
                translator = Translation.getClient(options)

                translate.setOnClickListener {
                    translate(translator, conditions)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        swap.setOnClickListener {
            lang1.setSelection(target)
            lang2.setSelection(selected)

            options = TranslatorOptions.Builder()
                .setSourceLanguage(selectedLang)
                .setTargetLanguage(targetLang)
                .build()
            translator = Translation.getClient(options)

            translate.setOnClickListener {
                translate(translator, conditions)
            }

            val a = selected
            selected = target
            target = a
        }

        output.movementMethod = ScrollingMovementMethod()

        if (args.text.isNotEmpty()) {
            input.setText(args.text)
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED){
            checkPermission()
        } else {
            Log.e("hello", "click")
            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(p0: Bundle?) {
                }

                override fun onBeginningOfSpeech() {
                    input.setText("Listening...")
                    Log.e("hello", "starttt")
                }

                override fun onRmsChanged(p0: Float) {
                }

                override fun onBufferReceived(p0: ByteArray?) {
                }

                override fun onEndOfSpeech() {
                }

                override fun onError(p0: Int) {
                }

                override fun onResults(p0: Bundle?) {
                    speech.setBackgroundResource(R.drawable.monogram)
                    val data = p0!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    input.setText(data!![0])
                    translate(translator, conditions)
                }

                override fun onPartialResults(p0: Bundle?) {
                    TODO("Not yet implemented")
                }

                override fun onEvent(p0: Int, p1: Bundle?) {
                    TODO("Not yet implemented")
                }
            })
        }

        speech.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                speech.setBackgroundResource(R.drawable.monogram)
                speechRecognizer.stopListening()
            }
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                Log.e("hello", "dd")
                speech.setImageResource(R.drawable.monogram_active)
                speechRecognizer.startListening(speechRecognizerIntent)
            }
            false
        }

        textToSpeech = TextToSpeech(
            requireContext()
        ) { i ->
            // if No error is found then only it will run
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                textToSpeech.language = Locale.forLanguageTag("es")
            }
        }

        voice.setOnClickListener {
            textToSpeech.speak(output.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    private fun checkPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.RECORD_AUDIO),
            recordAudioRequestCode
        )
    }

    private fun translate(translator: Translator, conditions: DownloadConditions) {
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(input.text.toString()).addOnSuccessListener {
                    output.text = it
                    binding.translated.visibility = View.VISIBLE
                    Log.e("hello", it)
                } .addOnFailureListener {
                    Log.e("hello", it.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("hello", exception.toString())
            }
    }
}