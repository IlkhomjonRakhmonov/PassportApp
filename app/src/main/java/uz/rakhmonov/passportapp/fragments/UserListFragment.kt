package uz.rakhmonov.passportapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import uz.rakhmonov.passportapp.MyData.Datam.listim
import uz.rakhmonov.passportapp.MyData.Datam.mienUserim
import uz.rakhmonov.passportapp.R
import uz.rakhmonov.passportapp.adapters.RV_adapter
import uz.rakhmonov.passportapp.adapters.rvClick
import uz.rakhmonov.passportapp.databinding.FragmentUserListBinding
import uz.rakhmonov.passportapp.myDB.AppDataBase
import uz.rakhmonov.passportapp.myDB.MyUser
import java.text.ParsePosition

class UserListFragment : Fragment() ,rvClick{

private val binding by lazy { FragmentUserListBinding.inflate(layoutInflater) }
    lateinit var appDataBase: AppDataBase
    lateinit var myRV_adapter: RV_adapter
    lateinit var userList:ArrayList<MyUser>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appDataBase=AppDataBase.getInstance(binding.root.context)
        userList=appDataBase.myDao().getAllUsers() as ArrayList<MyUser>
        myRV_adapter=RV_adapter(requireContext(),userList, this)
        binding.myRv.adapter=myRV_adapter
        rvActions()

        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.addUserFragment)

        }

        return binding.root

    }

    override fun edit(user: MyUser,position: Int) {
        mienUserim=user
        findNavController().navigate(R.id.addUserFragment)
    }

    override fun delete(user: MyUser,position: Int) {
        appDataBase.myDao().deleteUser(user)
        userList.remove(user)
        myRV_adapter.notifyItemRemoved(position)
        myRV_adapter.notifyItemRangeChanged(0,myRV_adapter.list.size-1)

    }
    private fun rvActions(){
        val itemtoachHelper=object :ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags=ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags=ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(dragFlags,swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               myRV_adapter.onItemMove(viewHolder.adapterPosition,target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myRV_adapter.onItemDissmiss(viewHolder.adapterPosition)
            }
        }
        ItemTouchHelper(itemtoachHelper).attachToRecyclerView(binding.myRv)
    }

}