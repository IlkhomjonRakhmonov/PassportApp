package uz.rakhmonov.passportapp.myDB

import androidx.room.*

@Dao
interface MyUserInterfDao {
    @Insert
    fun addUser(myUser: MyUser)

    @Query ("select * from MyUser")
    fun getAllUsers():List<MyUser>

    @Delete
    fun deleteUser(myUser: MyUser)

    @Update
    fun editUser(myUser: MyUser)



}