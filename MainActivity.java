package com.example.shoesshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuahangsach.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ValueEventListener {
    // khai báo 3 gridview dùng chung cho menu Navigation Buttom
    GridView grDanhSachSach;
    GridView grDanhSachSach2;
    GridView grDanhSachSach3;

    // khai báo 3 adapter của gridview dùng chung trong 3 menu trang chủ và danh sách đặt và lịch sử
    GiayAdapter2 giayAdapter2;
    GiayAdapter1 giayAdapter1;
    GiayAdapter2 giayAdapter3;

    // mảng dùng lưu các phần tử đối tượng sách trong mang tim kiem
    ArrayList<Giay>arrayListSach=new ArrayList<Giay>();

    //khai báo database firebase và đường dẫn
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //mảng dùng lưu vị trí đăt mua
    List<Giay>list=new ArrayList<Giay>();

    //mảng dùng lưu mảng đối tượng sách đã đặt trong firebase
    //danh sach
    ArrayList<Giay>arrayListGiay2=new ArrayList<Giay>();

    ArrayList<Giay>arrayListGiay3=new ArrayList<Giay>();
    //xoa
    ArrayList<Giay>arrayListGiay4=new ArrayList<Giay>();

    // email đăng nhập
    String emailDangNhap="";

    // vị trí xóa trong danh sách dặt
    int position1=0;

    Button btnUser;

    //menu navigation Buttom trong danh sách đặt
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                //menu trang chủ, dùng setContentView() để trở về màn hình các cuốn sách
                case R.id.navigation_home:
                    setContentView(R.layout.activity_main);
                    addControls();
                    addEvents();
                    fakeData();
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    databaseReference=firebaseDatabase.getReference().child("List");
                    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                    return true;
                    //menu danh sách đặt
                case R.id.navigation_dashboard:

                    // gridview 2 dùng chung một gridview cùng id R.id.grDanhSachSach
                    grDanhSachSach2=(GridView)findViewById(R.id.grDanhSachSach);
                    giayAdapter2 =new GiayAdapter2(MainActivity.this,R.layout.itemrow1);
                    giayAdapter2.addAll(arrayListGiay2);
                    grDanhSachSach2.setAdapter(giayAdapter2);
                    grDanhSachSach2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Giay giay = giayAdapter2.getItem(position);
                            position1=position;
                            Toast.makeText(MainActivity.this,"So Luong: "+ giay.getSoLuong()+",Dia Chi : "+ giay.getDiaChiMua()
                                    +"Email :"+ giay.getEmailDangNhap()+"vị trí "+position,Toast.LENGTH_LONG).show();

                        }
                    });
                    return true;
                    //menu đăng nhập đăng kí
                case R.id.navigation_notifications:
                    Intent intentChuyeSangDangNhap=new Intent(MainActivity.this,DangNhapActivity.class);
                    startActivity(intentChuyeSangDangNhap);
                    return true;
                    // menu lịch sử xóa
                case R.id.mnu_lichsu:
                    // gridview 3 dùng chung trong Navigation Buttom
                    grDanhSachSach3=(GridView) findViewById(R.id.grDanhSachSach);
                    giayAdapter3=new GiayAdapter2(MainActivity.this,R.layout.itemrow1);
                    giayAdapter3.addAll(arrayListGiay4);
                    grDanhSachSach3.setAdapter(giayAdapter3);
                    grDanhSachSach3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Giay giay =giayAdapter3.getItem(position);
                            Toast.makeText(MainActivity.this, giay.getTenSP()+ giay.getDiaChiMua(),Toast.LENGTH_LONG).show();
                        }
                    });
                return true;
            }
            return false;

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
        fakeData();

        // nếu bật phần mềm khi đó danh sách đặt rỗng, khi đó hiển thị lời chào
        if(list.size()==0) {
            Toast.makeText(MainActivity.this, "Xin chào các bạn!", Toast.LENGTH_SHORT).show();
        }
        // khởi tạo đường dẫn và database
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("List");
        databaseReference.addValueEventListener(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void fakeData() {

        //thêm các  phần tử đối tượng giay vào Adapter
        //hai lenh nay dung xoa du lieu trong Adapter va mang khi dung setContenView quay tro ve man hinh trang chu
        giayAdapter1.clear();
        arrayListSach.clear();
        giayAdapter1.add(new Giay(1,"Giày Sneaker nam","Giày thể thao nam siêu thoáng Sportmax SPM905626D OFF WHITE. Với thiết kế năng động thời trang phù hợp với các bạn trẻ. Viền trắng bao bọc xung quanh tạo tạo nên kiểu cách thời trang thời thượng. Chất liệu tốt.",500000,"",0,R.drawable.giay1,"",""));
        giayAdapter1.add(new Giay(2,"Giày thể thao","Giày thể thao nam siêu thoáng Sportmax SPM905626D OFF WHITE",120000,"",0,R.drawable.giay2,"",""));
        giayAdapter1.add(new Giay(3,"Giày nam new style","",120000,"",0,R.drawable.giay3,"",""));
        giayAdapter1.add(new Giay(4,"Giày nam sneaker 2019 - Pettino gv07","",120000,"",0,R.drawable.giay4,"",""));
        giayAdapter1.add(new Giay(5,"Giày converse cổ cao","",120000,"",0,R.drawable.giay5,"",""));
        giayAdapter1.add(new Giay(6,"Giày thể thao nam cao cấp","",120000,"",0,R.drawable.giay6,"",""));
        giayAdapter1.add(new Giay(7,"Giày thời trang nam","",120000,"",0,R.drawable.giay7,"",""));
        giayAdapter1.add(new Giay(8,"Giày thể thao style","",120000,"",0,R.drawable.giay8,"",""));
        giayAdapter1.add(new Giay(9,"Giày thể thao","",120000,"",0,R.drawable.giay9,"",""));
        giayAdapter1.add(new Giay(10,"Giày thể thao style","",120000,"",0,R.drawable.giay10,"",""));
        giayAdapter1.add(new Giay(11,"Giày nam sneaker","",120000,"",0,R.drawable.giay11,"",""));
        giayAdapter1.add(new Giay(12,"Giày nam Style","",120000,"",0,R.drawable.giay12,"",""));
        giayAdapter1.add(new Giay(13,"Giày Sneaker nam","",120000,"",0,R.drawable.giay13 ,"",""));
        giayAdapter1.add(new Giay(14,"Giày thể thao nam","",120000,"",0,R.drawable.giay14,"",""));
        giayAdapter1.add(new Giay(15,"Giày thể thao nam","",120000,"",0,R.drawable.giay15,"",""));
        giayAdapter1.add(new Giay(16,"Giày nam Style","",120000,"",0,R.drawable.giay3,"",""));
       // thêm các đối tượng sách vào mảng
       for(int i = 0; i< giayAdapter1.getCount(); i++){
            Giay giay = giayAdapter1.getItem(i);
            arrayListSach.add(giay);
        }
    }

    private void addControls() {

        // gridview 1 dùng chung một gridview cùng id R.id.grDanhSachSach
        grDanhSachSach=(GridView)findViewById(R.id.grDanhSachSach);
        giayAdapter1 =new GiayAdapter1(MainActivity.this,R.layout.itemrow1);
        grDanhSachSach.setAdapter(giayAdapter1);
    }

    private void addEvents() {
        grDanhSachSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Giay giay = giayAdapter1.getItem(position);
                moThongTin(giay);
            }
        });
    }
    // phương thức hiển thị layout chi tiết và đặt mua
    private void moThongTin(final Giay giay) {
        setContentView(R.layout.itemrow2);
        final EditText edtDiaChiMua=(EditText) findViewById(R.id.edtDiaChi);
        ImageView imgHinhAnh2=(ImageView)findViewById(R.id.imgHinhAnh2);
        TextView txtTenSP2=(TextView)findViewById(R.id.txtTenSP2);
        TextView txtGiaGiay2=(TextView)findViewById(R.id.txtGiaGiay2);
        TextView txtNhaXuatBan2=(TextView)findViewById(R.id.txtNhaSanXuat2);
        TextView txtThongTin2=(TextView)findViewById(R.id.txtGioiThieuSach);
        Button btnMua=(Button)findViewById(R.id.btnMua);
        Button btnTroVe=(Button) findViewById(R.id.btnTroVe);

        imgHinhAnh2.setImageResource(giay.getHinhAnh());
        txtTenSP2.setText(giay.getTenSP());
        txtGiaGiay2.setText(giay.getGiaGiay()+"VND");
        txtNhaXuatBan2.setText(giay.getNhaSanXuat());
        txtThongTin2.setText(giay.getGioiThieu());
        btnMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nếu chưa đặt hàng thêm danh sách đặt để tiếp tục vị trí đặt tiếp theo
                if (list.size() == 0) {
                    list.addAll(arrayListGiay3);
                }
                giay.setDiaChiMua(edtDiaChiMua.getText().toString());
                // nhận email từ màn hình dăng nhập
                    Intent intentNhanEmail = getIntent();
                    emailDangNhap = intentNhanEmail.getStringExtra("emailDangNhap");
                giay.setEmailDangNhap(emailDangNhap);
                //nếu có email mới thêm đối tượng giay, nếu không hiển thị thông báo
                    if (emailDangNhap != null) {
                            giay.setSoLuong(giay.getSoLuong() + 1);
                            list.add(giay);
                            Toast.makeText(MainActivity.this,"So Luong"+ giay.getSoLuong(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Bạn Phải Đăng Nhập!", Toast.LENGTH_LONG).show();

                    }
                    // thêm mảng đặt vào cơ sở dữ liệu
                    databaseReference.setValue(list);
            }
        });
        btnTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                addControls();
                addEvents();
                fakeData();
                firebaseDatabase=FirebaseDatabase.getInstance();
                databaseReference=firebaseDatabase.getReference().child("List");
                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mnu_search,menu);

        MenuItem mnusearch=menu.findItem(R.id.mnu_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(mnusearch);
        MenuItemCompat.setOnActionExpandListener(mnusearch, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Giay>dsTim=new ArrayList<Giay>();
                for(Giay giay1 :arrayListSach){
                    //tìm theo tên sách
                    if(giay1.getTenSP().contains(newText)) {
                        dsTim.add(giay1);
                    }
                }
                    // xóa Adapter danh sách tìm
                    giayAdapter1.clear();
                //thêm mảng tìm thấy vào Adapter
                    giayAdapter1.addAll(dsTim);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mnu_thoat:
                // thoát toàn bộ trương trình
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();
                break;
            case R.id.mnu_XoaItem:
                    // lấy vị trí position1 trong Adapter danh sách đặt
                    Giay giayXoa = giayAdapter2.getItem(position1);
                    giayAdapter2.remove(giayXoa);
                    // do mảng trong cơ sở dữ liệu firebase lấy theo mảng trong gridview nên có vị trí giống nhau
                    databaseReference.child(giayXoa.getId() + "").child("tenGiay").setValue("Đã xóa!");
                    list.clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        // lấy email từ màn hình đăng nhập
        Intent intentNhanEmail=getIntent();
        String emailDangNhap2=intentNhanEmail.getStringExtra("emailDangNhap");
        ArrayList<Giay> giay3 =new ArrayList<Giay>();
        ArrayList<Giay> giay4 =new ArrayList<Giay>();
        ArrayList<Giay> giay5 =new ArrayList<Giay>();
        // các phần tử trong cơ sở dữ liệu firebase và ép kiểu về đối tượng sách ôn thi
        Iterable<DataSnapshot> dataSnapshots=dataSnapshot.getChildren();
        int i=-1;
        for(DataSnapshot dataSnapshot1:dataSnapshots) {
            i=i+1;
            Giay giay = dataSnapshot1.getValue(Giay.class);
            giay.setId(i);
            giay4.add(giay);
            // nếu email đã có thì mới thêm phần tử sách theo email tương ứng vào danh sách đặt theo user
            if((emailDangNhap2!=null)){
                if((!giay.getTenSP().contains("Đã xóa!"))&& giay.getEmailDangNhap().contains(emailDangNhap2)){
                        giay3.add(giay);
                }
                // nếu đã xóa đối tượng thì thêm đối tượng xóa vào lịch sử
                if(giay.getTenSP().contains("Đã xóa!")&&(giay.getEmailDangNhap().contains(emailDangNhap2))){
                    giay5.add(giay);
                }

            }

        }
        // thêm danh sách đặt theo user
        arrayListGiay2.clear();
        arrayListGiay2.addAll(giay3);

        arrayListGiay3.clear();
        arrayListGiay3.addAll(giay4);
        // thêm danh sách lịch sử xóa theo user
        arrayListGiay4.clear();
        arrayListGiay4.addAll(giay5);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
