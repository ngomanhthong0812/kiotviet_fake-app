package com.example.kiotviet_fake.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiotviet_fake.R;
import com.example.kiotviet_fake.activities.LoginActivity;
import com.example.kiotviet_fake.activities.MainActivity;
import com.example.kiotviet_fake.activities.ProductDetailActivity;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderAPI;
import com.example.kiotviet_fake.database.deleteItems.DeleteItemOfOrderService;
import com.example.kiotviet_fake.models.Order;
import com.example.kiotviet_fake.models.Product;
import com.example.kiotviet_fake.session.SessionManager;

import java.text.ParseException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewHolder> {
    ArrayList<Product> products;
    Context context;

    public ProductAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new viewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Product product = products.get(position);
        SessionManager sessionManager = SessionManager.getInstance();
        int getOrderQuantityByProductId = sessionManager.getOrderQuantityByIdProductItem(product.getIdProductItem());
        final int[] count = {getOrderQuantityByProductId > 0 ? getOrderQuantityByProductId : 1};

        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(String.valueOf(product.getPrice()));
        holder.txtQuantity.setText(String.valueOf(product.getQuantityOrder()));
        holder.txtQuantity.setVisibility(product.getQuantityOrder() > 0 ? View.VISIBLE : View.GONE);
        holder.imgSelect.setVisibility(product.getQuantityOrder() > 0 ? View.VISIBLE : View.GONE);

        holder.txtCount.setText(String.valueOf(getOrderQuantityByProductId > 0 ? getOrderQuantityByProductId : 1));
        holder.countQuanity.setVisibility(getOrderQuantityByProductId > 0 ? View.VISIBLE : View.GONE);
        holder.imgCheck.setVisibility(getOrderQuantityByProductId > 0 ? View.VISIBLE : View.GONE);

        // xử lý item order product
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!product.getIdProductItem().equals("") && count[0] > 0) {
                    holder.countQuanity.setVisibility(View.VISIBLE);
                    holder.imgCheck.setVisibility(View.VISIBLE);
                    product.setQuantityOrder(1); // gán số lượng 1 khi chọn vào sản phẩm

                    // thêm đơn hàng mới
                    Order order1 = new Order(product.getIdProductItem(), product.getQuantityOrder(), product.getPrice(), 1, product.getId());
                    sessionManager.addOrder(order1);
                } else {
                    holder.countQuanity.setVisibility(View.GONE);
                    holder.imgCheck.setVisibility(View.GONE);
                    product.setQuantityOrder(0);  // gán số lượng 0 khi ko chọn vào sản phẩm

                    //xóa đơn hàng
                    SessionManager sessionManager = SessionManager.getInstance();
                    sessionManager.removeOrderByProductId(product.getIdProductItem());
                }

            }

        });

        holder.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]++;
                holder.txtCount.setText(String.valueOf(count[0]));
                product.setQuantityOrder(count[0]);
                sessionManager.updateQuantityProduct(product.getIdProductItem(), count[0]);
            }
        });

        holder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[0] > 1) {
                    count[0]--;
                    holder.txtCount.setText(String.valueOf(count[0]));
                    product.setQuantityOrder(count[0]);
                    sessionManager.updateQuantityProduct(product.getIdProductItem(), count[0]);
                }
            }
        });


        // xử lý item detail product
        holder.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_more) {
                            // Xử lý khi người dùng chọn sửa
                            Intent intent = new Intent(context, ProductDetailActivity.class);

                            intent.putExtra("product_id", product.getId());
                            intent.putExtra("product_name", product.getName());
                            intent.putExtra("product_price", product.getPrice());
                            intent.putExtra("product_quantity", product.getQuantityOrder());

                            System.out.println("test SLL produxt:" + product.getQuantityOrder());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Thêm cờ FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent); // Sử dụng context để khởi chạy Intent

                        }
                        if (item.getItemId() == R.id.action_delete) {
                            // Xử lý khi người dùng chọn sửa
                            Log.e("TAG", "onMenuItemClick: " + product.getId());
                            DeleteOrderProductItem("11168851", "60-dayfreetrial");
                            return true;
                        }
                        return false;
                    }

                    private void DeleteOrderProductItem(String username, String password)  {

                        DeleteItemOfOrderService service =  DeleteItemOfOrderAPI.createService(username, password);
                        Call<String> call = service.deleteItemOfOrder(product.id);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()) {
                                    // Xử lý phản hồi thành công từ API nếu cần
                                    Log.d("DeleteItemOfOrder", "Xóa sản phẩm thành công id  : " + product.getId());
                                    products.remove(product);
                                    notifyDataSetChanged();

                                } else {
                                    // Xử lý phản hồi lỗi từ API nếu cần
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                    }
                });
                popupMenu.show();

            }
        });
    }

    public int getItemCount() {
        return products.size();
    }

    public class  viewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity, btnTang, btnGiam, txtCount;
        ImageView imgSelect, imgCheck;
        LinearLayout countQuanity;
        RelativeLayout item;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtQuantity = (TextView) itemView.findViewById(R.id.txtQuantity);
            imgSelect = (ImageView) itemView.findViewById(R.id.imgSelect);
            imgCheck = (ImageView) itemView.findViewById(R.id.imgCheck);
            item = (RelativeLayout) itemView.findViewById(R.id.item);
            countQuanity = (LinearLayout) itemView.findViewById(R.id.countQuanity);
            btnTang = (TextView) itemView.findViewById(R.id.btnTang);
            btnGiam = (TextView) itemView.findViewById(R.id.btnGiam);
            txtCount = (TextView) itemView.findViewById(R.id.txtCount);

        }
    }


}