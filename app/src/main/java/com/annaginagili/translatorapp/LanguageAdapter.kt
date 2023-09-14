package com.annaginagili.translatorapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView

class LanguageAdapter(context: Context, languageList: ArrayList<LanguageItem>) :
    ArrayAdapter<LanguageItem>(context, 0, languageList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        val conView = LayoutInflater.from(context).inflate(R.layout.custom_spinner, parent, false)
        val language = conView.findViewById<TextView>(R.id.language)
        val flag = conView.findViewById<ShapeableImageView>(R.id.flag)
        val currentItem: LanguageItem? = getItem(position)

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            language.text = currentItem.language
            flag.setBackgroundResource(currentItem.flag)
        }
        return conView
    }
}