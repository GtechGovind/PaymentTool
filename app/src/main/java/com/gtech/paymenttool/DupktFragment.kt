package com.gtech.paymenttool

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.gtech.paymenttool.databinding.FragmentDupktBinding

class DupktFragment : Fragment() {

    private var _binding: FragmentDupktBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDupktBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Generate.setOnClickListener {

            if (isValid()) {

                try {
                    binding.DerivedIpeck.text = ""

                    val bdk = Dukpt.toBitSet(binding.BDK.text.toString().trim().toByteArray())
                    val ksn = Dukpt.toBitSet(binding.KSN.text.toString().trim().toByteArray())

                    println(bdk)
                    println(ksn)

                    val result = Dukpt.getIpek(bdk, ksn)
                    binding.DerivedIpeck.text = Dukpt.toHex(result.toByteArray())
                } catch (e: Exception) {
                    binding.DerivedIpeck.text = e.message
                }

            }

        }

        binding.CopyToClipboardBtn.setOnClickListener {
            val clipboardManager = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("IPEK", binding.DerivedIpeck.text.toString())
            clipboardManager.setPrimaryClip(clipData)
        }

    }

    private fun isValid(): Boolean {

        val bdk = binding.BDK.text.toString()
        val ksn = binding.KSN.text.toString()

        if (bdk.isEmpty()) {
            binding.BDKLayout.error = "Bdk is required !"
        } else if (bdk.length < 32) {
            binding.BDKLayout.error = "Bdk must be of min 32 digits !"
        } else if (ksn.isEmpty()) {
            binding.KSNLayout.error = "Ksn is required !"
        } else if (ksn.length < 16) {
            binding.KSNLayout.error = "Ksn must be of min 16 digits !"
        } else {
            return true
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}