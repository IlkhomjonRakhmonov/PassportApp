package uz.rakhmonov.passportapp.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.rakhmonov.passportapp.MyData.Datam.mienUserim
import uz.rakhmonov.passportapp.myDB.MyUser
import uz.rakhmonov.passportapp.databinding.MyRvItemBinding
import uz.rakhmonov.passportapp.myDB.AppDataBase
import java.util.*
import kotlin.collections.ArrayList

class RV_adapter (var context:Context,val list:ArrayList<MyUser>,var rvClick: rvClick):RecyclerView.Adapter<RV_adapter.VH>(),myItemTouchHelperAdapter{
        lateinit var appDataBase: AppDataBase
    inner class VH (val rvItemBinding: MyRvItemBinding): RecyclerView.ViewHolder(rvItemBinding.root){
        fun onHolder(user: MyUser, position:Int){
            rvItemBinding.name.text=user.name
            rvItemBinding.surname.text=user.surname
            rvItemBinding.image.setImageURI(Uri.parse(user.image))
            rvItemBinding.numbPassport.text=user.number_passport
            appDataBase= AppDataBase.getInstance(rvItemBinding.root.context)

            rvItemBinding.delete.setOnClickListener {
                rvClick.delete(user,position)
            }
            rvItemBinding.edit.setOnClickListener {
                rvClick.edit(user,position)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(MyRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onHolder(list[position],position)

    }

    override fun getItemCount(): Int=list.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition>toPosition){
            for (i in fromPosition until toPosition){
                Collections.swap(list,i,i)
            }
        }else{
            for (i in fromPosition until toPosition+1){
                Collections.swap(list,i,i)
            }
        }
        notifyItemMoved(fromPosition,toPosition)
        appDataBase.myDao().editUser(appDataBase.myDao().getAllUsers()[0])
    }

    override fun onItemDissmiss(position: Int) {
        appDataBase = AppDataBase.getInstance(context)
        list.removeAt(position)
        notifyItemRemoved(position)
        appDataBase.myDao().deleteUser(appDataBase.myDao().getAllUsers()[position]!!)

    }


}
interface rvClick{
    fun edit(user: MyUser,position: Int)
    fun delete(user: MyUser,position: Int)
}
interface myItemTouchHelperAdapter {
    fun onItemMove(fromPosition:Int,toPosition:Int)
    fun onItemDissmiss(position:Int)
}