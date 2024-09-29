package com.example.pemesanantiket_6

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.NumberFormat
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.example.pemesanantiket_6.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tujuan: Array<String> //berisi nama-nama tujuan perjalanan, yang diambil dari file resource XML.
    private lateinit var harga: IntArray  // Integer array untuk menyimpan harga

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi array tujuan dan harga dari resources
        tujuan = resources.getStringArray(R.array.tujuan)
        harga = resources.getIntArray(R.array.harga)

        // Set adapter untuk spinner tujuan
        val adapterTujuan = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            tujuan
        )
        binding.spinnerTujuan.adapter = adapterTujuan

        // Set onClickListener untuk memilih tanggal
        binding.showTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        // Set onClickListener untuk memilih jam keberangkatan
        binding.showJam.setOnClickListener {
            showTimePickerDialog()
        }

        // Set onClickListener untuk memesan tiket
        binding.btnPesanTiket.setOnClickListener {
            showConfirmationDialog()
        }
    }

    // Fungsi untuk menampilkan DatePickerDialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format tanggal yang dipilih
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                // Tampilkan tanggal di EditText
                binding.showTanggal.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    // Fungsi untuk menampilkan TimePickerDialog
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.showJam.setText(formattedTime)
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    // Fungsi untuk menampilkan dialog konfirmasi pemesanan
    private fun showConfirmationDialog() {
        // Ambil tujuan yang dipilih
        val selectedIndex = binding.spinnerTujuan.selectedItemPosition

        // Pastikan selectedIndex valid dan harga sesuai diambil dari array
        if (selectedIndex >= 0 && selectedIndex < harga.size) {
            // Ambil harga berdasarkan tujuan yang dipilih
            val selectedHarga = harga[selectedIndex]

            // Format harga menggunakan NumberFormat
            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            val formattedHarga = formatRupiah.format(selectedHarga)

            // Buat AlertDialog untuk konfirmasi pemesanan
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi Pemesanan")
            builder.setMessage("Tarif Tiket: $formattedHarga")

            // Tombol Batal
            builder.setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }

            // Tombol Beli
            builder.setPositiveButton("Beli") { dialog, _ ->
                // Aksi ketika tombol Beli ditekan, buat Intent ke SuccessActivity
                val intent = Intent(this, SuccessActivity2::class.java)

                // Kirim data seperti nama, jam, tanggal, dan tujuan ke SuccessActivity
                intent.putExtra("NAMA", binding.etUsername.text.toString())
                intent.putExtra("JAM", binding.showJam.text.toString())
                intent.putExtra("TANGGAL", binding.showTanggal.text.toString())
                intent.putExtra("TUJUAN", binding.spinnerTujuan.selectedItem.toString())

                // Pindah ke SuccessActivity
                startActivity(intent)

                dialog.dismiss()
            }

            // Tampilkan dialog
            val dialog = builder.create()
            dialog.show()
        } else {
            // Tampilkan pesan jika tujuan tidak valid
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("Tujuan tidak valid. Silakan pilih tujuan yang tersedia.")
            builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }
    }
}