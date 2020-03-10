package com.example.magicthegathering

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.magicthegathering.ui.cards.list.ListCardsFragment
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ListCardsFragment.newInstance())
                    .commitNow()
        }
    }
}
