package aarambh.apps.blinkitcloneadmin.activity

import aarambh.apps.blinkitcloneadmin.R
import aarambh.apps.blinkitcloneadmin.databinding.ActivityAdminMainBinding
import aarambh.apps.blinkitcloneadmin.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.bottomMenu, Navigation.findNavController(this,R.id.fragmentContainerView2))
    }
}