package uz.rakhmonov.passportapp.myDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyUser::class], version = 2)
 abstract class AppDataBase :RoomDatabase(){

     abstract fun myDao():MyUserInterfDao

     companion object {
         private var instance:AppDataBase?=null
        @Synchronized
         fun getInstance(context: Context):AppDataBase{
             if (instance==null){
                 instance=Room.databaseBuilder(context,AppDataBase::class.java,"myUser_DB")
                     .fallbackToDestructiveMigration()
                     .allowMainThreadQueries()
                     .build()
             }

             return instance!!
         }
     }
}

