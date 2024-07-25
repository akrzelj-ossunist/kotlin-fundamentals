// TaskAdapter.kt
package com.example.todoapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.Task

class TaskAdapter(private val tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val priorityTextView: TextView = view.findViewById(R.id.priorityTextView)
        val statusButton: Button = view.findViewById(R.id.statusButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.descriptionTextView.text = task.description
        holder.priorityTextView.text = "Priority: ${task.priority}"
        holder.statusButton.text = if (task.isCompleted) "Completed" else "Finish"

        holder.statusButton.setOnClickListener {
            if (!task.isCompleted) {
                task.isCompleted = true
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}
