package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CartFragment;
import cn.ucai.fulicenter.fragment.CategoryFragment;
import cn.ucai.fulicenter.fragment.GoodsFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.fragment.PersonalFragment;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.layout_new_good)
    RadioButton mLayoutNewGood;
    @Bind(R.id.layout_boutique)
    RadioButton mLayoutBoutique;
    @Bind(R.id.layout_category)
    RadioButton mLayoutCategory;
    @Bind(R.id.layout_cart)
    RadioButton mLayoutCart;
    @Bind(R.id.personal)
    RadioButton mPersonal;

    RadioButton[] mRbArray;
    int mIndex;
    int mCurrentIndex;


    Fragment[] mFragment;
    GoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    PersonalFragment mPersonalFragment;
    CartFragment mCartFragment;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //    L.i("MainActivity onCreate");
       /* initView();
        initFragment();*/
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        initFragment();
    }

    private void initFragment() {
        mFragment = new Fragment[5];
        mNewGoodsFragment = new GoodsFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mPersonalFragment = new PersonalFragment();
        mCartFragment = new CartFragment();
        mFragment[0] = mNewGoodsFragment;
        mFragment[1] = mBoutiqueFragment;
        mFragment[2] = mCategoryFragment;
        mFragment[3] = mCartFragment;
        mFragment[4] = mPersonalFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mNewGoodsFragment)
                .add(R.id.fragment_container,mBoutiqueFragment)
                .add(R.id.fragment_container,mCategoryFragment)
                .hide(mBoutiqueFragment)
                .hide(mCategoryFragment)
                .hide(mPersonalFragment)
                .show(mNewGoodsFragment)
                .commit();

    }
    @Override
    protected void initView() {
        mRbArray=new RadioButton[5];
        mRbArray[0] = mLayoutNewGood;
        mRbArray[1] = mLayoutBoutique;
        mRbArray[2] = mLayoutCategory;
        mRbArray[3] = mLayoutCart;
        mRbArray[4] = mPersonal;

    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                mIndex = 0;
               /* NewGoodsFragment newGoodsFragment = new NewGoodsFragment();
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, newGoodsFragment);
                mFragmentTransaction.commit();*/
                break;
            case R.id.layout_boutique:
                mIndex = 1;
                break;
            case R.id.layout_category:
                mIndex = 2;
                break;
            case R.id.layout_cart:
                if (FuLiCenterApplication.getUser() == null) {
                    MFGT.gotoLoginActivityFromCart(this);
                } else {
                    mIndex = 3;
                }
                break;
            case R.id.personal:
                if (FuLiCenterApplication.getUser() == null) {
                    MFGT.gotoLoginActivity(this);
                } else {
                    mIndex = 4;
                }
                break;
        }
        setFragment();

    }

    private void setFragment() {
        if (mIndex != mCurrentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragment[mCurrentIndex]);
            if (!mFragment[mIndex].isAdded()) {
                ft.add(R.id.fragment_container, mFragment[mIndex]);
            }
            ft.show(mFragment[mIndex]).commitAllowingStateLoss();
        }
        setRadioButtonStatus();
        mCurrentIndex=mIndex;
    }

    private void setRadioButtonStatus() {

        for (int i=0;i<mRbArray.length;i++) {
            if (i==mIndex) {
                mRbArray[i].setChecked(true);
            } else {
                mRbArray[i].setChecked(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mIndex == 4 && FuLiCenterApplication.getUser() == null) {
            mIndex = 0;
        }
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e(TAG, "onActivityResult,requestCode" + requestCode);
        if ( FuLiCenterApplication.getUser()!=null) {
            if (requestCode == I.REQUEST_CODE_LOGIN) {
                mIndex = 4;
            } else if (requestCode == I.REQUEST_CODE_CART) {
                mIndex = 3;
            }
        }
    }
}
