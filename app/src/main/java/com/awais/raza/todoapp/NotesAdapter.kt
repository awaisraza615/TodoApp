package com.awais.raza.todoapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awais.raza.todoapp.databinding.ItemViewBinding


class NotesAdapter(
    private val notes: ArrayList<Notes?>,
    private val onClickListener: OnClickListener
)
    : RecyclerView.Adapter<NotesAdapter.HoursViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        val binding = ItemViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return HoursViewHolder(binding)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        with(holder){
            with(notes[position]) {
                this?.let {
                    binding.textView.text = task
                    binding.checkbox.isChecked = isChecked

                    if (isChecked){
                        binding.textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    }else{
                        binding.textView.paintFlags = 0

                    }


                }
                binding.deleteBtn.setOnClickListener{
                    this?.let { it1 -> onClickListener.onDeleteClick(pos = position, note = it1) }
                }
                binding.checkbox.setOnCheckedChangeListener { _, isChecked ->

                    if (isChecked) {


                            binding.textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG


                        this?.let { onClickListener.onIsCheckedClick(pos = position, note = it) }
                    } else {

                            binding.textView.paintFlags = 0


                        this?.let { onClickListener.onIsUnCheckedClick(pos = position, note = it) }
                    }
                }
            }
        }
    }

    inner class HoursViewHolder(val binding: ItemViewBinding)
        :RecyclerView.ViewHolder(binding.root)

}


interface OnClickListener {
    fun onIsCheckedClick(pos:Int,note: Notes)
    fun onIsUnCheckedClick(pos:Int,note: Notes)
    fun onDeleteClick(pos:Int,note: Notes)
}