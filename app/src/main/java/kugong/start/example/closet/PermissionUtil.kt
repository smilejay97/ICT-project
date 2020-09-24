package kugong.start.example.closet

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ContextUtils.getActivity

class PermissionUtil {
//한번에 원하는 모든 permission을 입력하고 현재 부족한 것만 요청하는 permission 클래스

    fun requestPermission(
        context: Context, requestCode: Int, vararg permissions: String
    ):Boolean{
        var granted = true
        val permissionNeeded = ArrayList<String>()

        permissions.forEach {
            val permissionCheck = ContextCompat.checkSelfPermission(context,it)
            val hasPermission = permissionCheck == PackageManager.PERMISSION_GRANTED
            granted = granted and hasPermission
            if(!hasPermission){
                permissionNeeded.add(it)
            }
        }

        if(granted){
            return true
        }else {
            getActivity(context)?.let {
                requestPermissions(
                    it
                    ,permissionNeeded.toTypedArray()
                    ,requestCode
                )
            } //permissions는 String인데 permissionNeeded는 ArrayList라서 변환해준거임
            return false
        }
    }

    fun permissionGranted(
        requestCode : Int, permissionCode : Int, grantResult : IntArray
    ) : Boolean{
        return requestCode == permissionCode
                && grantResult.size > 0
                && grantResult[0] == PackageManager.PERMISSION_GRANTED
    }
}