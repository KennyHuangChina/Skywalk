package com.kjs.skywalk.communicationdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kenny on 2017/6/16.
 */

public class FileSelectDialog extends DialogFragment {
    private TextView mTxtvCurrentPath;
    private File mCurrentPath;
    private ListView mLstvFiles;
    //    private ArrayAdapter<String> mAdptFiles;
    private SimpleAdapter mSimAdptFiles;
    private ArrayList<String> mListFiles = new ArrayList<String>();
    private Button mButnConfirm;
    private String mSelectedFileName;
    private int mSelectedFilePos = -1;
    private ArrayList<Map<String, Object>> mAllList = new ArrayList<Map<String, Object>>();

    public FileSelectDialog() {
    }

    public static final FileSelectDialog newInstance(String default_path ) {
        FileSelectDialog ofd = new FileSelectDialog();
        Bundle bundle = new Bundle();
        bundle.putString("default_path", default_path);
        ofd.setArguments(bundle);
        return  ofd;
    }

    private void checkDefaultPath(String default_path) {
        if(default_path != null && !default_path.isEmpty()) {
            File file = new File(default_path);
            if(!file.exists() || file.isFile()) {
                // try folder
                String folder = default_path.substring(0, default_path.lastIndexOf("/") + 1);
                File fFolder = new File(folder);
                if(fFolder.exists())
                    mCurrentPath = fFolder;

            } else if (file.isDirectory()) {
                mCurrentPath = file;
            }
        }
    }

    public interface DialogConfirmCallback {
        void onDialogConfirm(DialogFragment dialog, Bundle arg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // check default path
        String default_path = getArguments().getString("default_path");
        checkDefaultPath(default_path);
        //
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_openfile, null);

        mTxtvCurrentPath = (TextView) contentView.findViewById(R.id.txtv_openfdlg_current_path);
        mLstvFiles = (ListView) contentView.findViewById(R.id.lstv_openfdlg_files);
        mButnConfirm = (Button) contentView.findViewById(R.id.butn_openfdlg_confirm);

//        mAdptFiles = new ArrayAdapter<String>(getActivity(),
//                R.layout.listitem_file,
//                R.id.txtv_file_list_item,
//                mListFiles);
//        mLstvFiles.setAdapter(mAdptFiles);


        mSimAdptFiles = new SimpleAdapter(getActivity(),
                mAllList,
                R.layout.listitem_file,
                new String[]{"icon", "name"},
                new int[]{R.id.iv_icon, R.id.txtv_file_list_item});
        mLstvFiles.setAdapter(mSimAdptFiles);

        if(mCurrentPath == null) {
            if (isExternalStorageReadable()) {
                mCurrentPath = Environment.getExternalStorageDirectory();
            }
            else {
                mCurrentPath = Environment.getDataDirectory();
            }
        }
        updateFileList(-1, mCurrentPath);

        mLstvFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position < 0 || position >= mListFiles.size()) {
//                    return;
//                }
//                String newPathName = mListFiles.get(position);
                if (position < 0 || position >= mAllList.size()) {
                    return;
                }
                String newPathName = (String) mAllList.get(position).get("name");
                File newPath;
                if (newPathName.equals("..")) {
                    newPath = mCurrentPath.getParentFile();
                } else {
                    newPath = new File(mCurrentPath, newPathName);
                }
                if (newPath.isFile()) {
                    selectFile(position, newPath);
                }
                else {
                    updateFileList(position, newPath);
                }
            }
        });

        mButnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmListener != null) {
                    Bundle arg = new Bundle();
                    arg.putString("file path", mSelectedFileName);
                    mConfirmListener.onDialogConfirm(FileSelectDialog.this, arg);
                }
                dismiss();
            }
        });

        Dialog dlg = builder.create();
        dlg.show();
        dlg.getWindow().setContentView(contentView);
        dlg.hide();

        return dlg;


//        builder.setView(contentView)
//                .setTitle(R.string.openfdlg_title_choose_file);
//        return builder.create();
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void selectFile(int position, File file) {
        mSelectedFilePos = position;
        mSelectedFileName = file.getAbsolutePath();
        mButnConfirm.setEnabled(true);
        mSimAdptFiles.notifyDataSetChanged();
    }

    private void updateFileList(int position, File path) {
        if (position >= 0) {
            mLstvFiles.setItemChecked(position, false);
        }
        if (mSelectedFilePos >= 0) {
            mSelectedFilePos = -1;
            mSelectedFileName = null;
        }

        if(path == null)
            return;

        if (path.canRead()) {
            mButnConfirm.setEnabled(false);
            mSelectedFilePos = -1;
            mSelectedFileName = null;
            mListFiles.clear();
            mListFiles.add("..");

            mAllList.clear();

            ArrayList<Map<String, Object>> lstFolder = new ArrayList<Map<String, Object>>();
            ArrayList<Map<String, Object>> lstFile = new ArrayList<Map<String, Object>>();

            File[] files = path.listFiles();
            if(files != null) {
                for(File file : files) {
                    if(file.isDirectory()) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("icon", R.drawable.icn_folder);
                        item.put("name", file.getName());
                        item.put("path", file.getPath());
                        lstFolder.add(item);
                    } else if(file.isFile()) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("icon", R.drawable.icn_file);
                        item.put("name", file.getName());
                        item.put("path", file.getPath());
                        lstFile.add(item);
                    }
                }

                File parent = path.getParentFile();
                if(parent != null && parent.canRead()) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("icon", R.drawable.icn_folder);
                    item.put("name", "..");
                    item.put("path", parent.getPath());
                    mAllList.add(item);
                }
                mAllList.addAll(lstFolder);
                mAllList.addAll(lstFile);
            }


//            String[] fileList = path.list();
//            if (fileList != null) {
//                for (String fileName : fileList) {
//                    if (!fileName.startsWith(".")) {
//                        mListFiles.add(fileName);
//                    }
//                }
//            }
            mCurrentPath = path;
            mSimAdptFiles.notifyDataSetChanged();
            mTxtvCurrentPath.setText(mCurrentPath.getAbsolutePath());
        }
        else {
            Toast.makeText(getActivity(),
                    "Cannot access this path",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private DialogConfirmCallback mConfirmListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mConfirmListener = (DialogConfirmCallback)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ScanDialogConfirmCallback");
        }
    }
}
