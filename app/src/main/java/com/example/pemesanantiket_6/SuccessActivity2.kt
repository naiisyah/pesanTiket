package com.example.pemesanantiket_6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pemesanantiket_6.databinding.ActivitySuccess2Binding

class SuccessActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivitySuccess2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccess2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val nama = intent.getStringExtra("NAMA")
        val jam = intent.getStringExtra("JAM")
        val tanggal = intent.getStringExtra("TANGGAL")
        val tujuan = intent.getStringExtra("TUJUAN")

        // Tampilkan data di TextView
        binding.oyNama.text = "Nama: $nama"
        binding.oyJam.text = "Jam: $jam"
        binding.oyTanggal.text = "Tanggal: $tanggal"
        binding.oyTujuan.text = "Tujuan: $tujuan"
    }
}