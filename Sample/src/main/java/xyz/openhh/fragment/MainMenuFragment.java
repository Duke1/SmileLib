package xyz.openhh.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.openhh.R;
import xyz.openhh.activity.HelpActivity;
import xyz.openhh.activity.MainActivity;

/**
 * @author HH
 */
public class MainMenuFragment extends BaseFragment {

    @Bind(R.id.user_img)
    SimpleDraweeView sdvUserImg;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_mail)
    TextView tvMail;
    @Bind(R.id.linear_head)
    LinearLayout mLinearHead;
    @Bind(R.id.exit)
    TextView exit;
    @Bind(R.id.login)
    TextView login;


    private MainActivity mMainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        ButterKnife.bind(this, rootView);
        setUserInfodata();
        return rootView;
    }


    /**
     * 设置用户信息
     */
    private void setUserInfodata() {
        tvName.setText("Duke");
        tvMail.setText("email@163.com");

        sdvUserImg.setImageURI(Uri.parse("res://xyz.openhh/" + R.mipmap.zhanghan));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @OnClick(R.id.help_center)
    protected void onHelpCenterClick(View view) {


        HelpActivity.launch(mMainActivity);
    }


    @OnClick(R.id.about)
    protected void doAboutClicked() {
        Toast.makeText(getContext(), "doAboutClicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.archives_manage_label)
    protected void onArchivesManageClicked(View view) {
        if (null != mMainActivity) {
            mMainActivity.setFundationName(mMainActivity.getString(R.string.archives_manage_label_str));
            mMainActivity.openFundationFragment(new ArchiveManageFragment(), null);
        }
    }

    @OnClick(R.id.contract_manage_label)
    protected void onContractManageClicked(View view) {
        if (null != mMainActivity) {
            mMainActivity.setFundationName(mMainActivity.getString(R.string.contract_manage_label_str));
            mMainActivity.openFundationFragment(new ContractManageFragment(), null);
        }
    }


    @OnClick(R.id.login)
    protected void onLoginBtnLogin(View view) {
        login.setVisibility(View.INVISIBLE);
        exit.setVisibility(View.VISIBLE);
        mLinearHead.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.exit)
    protected void onExitBtnLogin(View view) {
        login.setVisibility(View.VISIBLE);
        exit.setVisibility(View.INVISIBLE);
        mLinearHead.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
