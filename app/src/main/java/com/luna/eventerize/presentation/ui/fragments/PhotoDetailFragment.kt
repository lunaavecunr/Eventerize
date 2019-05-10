package com.luna.eventerize.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.luna.eventerize.R
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.PhotoDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photo_detail.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PHOTO_URL = "PHOTO_URL"
private const val PHOTO_ID = "PHOTO_ID"
private const val EVENT_ID = "EVENT_ID"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PhotoDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PhotoDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PhotoDetailFragment : BaseFragment<PhotoDetailViewModel>(){
    private var photoUrl: String? = null
    private var photoId: String? = null
    private var eventId: String? = null
    private var navigator: Navigator? = null
    override val viewModelClass = PhotoDetailViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoUrl = it.getString(PHOTO_URL)
            photoId = it.getString(PHOTO_ID)
            eventId = it.getString(EVENT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = Navigator(fragmentManager!!)
        activity!!.title = ""
        fragment_photo_detail_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(fragment_photo_detail_toolbar)

        Picasso.get().load(photoUrl).into(fragment_photo_detail_photo_view)

        viewModel.getSuccess.observe(this, Observer {
            viewModel.destroyPicture(eventId!!)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.photo_details_fragment_options_menu,menu)
    }

    private fun deletePicture(){
        val dialog = AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.photo_destroy_operation))
            .setMessage(getString(R.string.photo_destroy_operation_text))
            .setPositiveButton(
                getString(R.string.yes_destroy_it)
            ) { _, _ ->
                viewModel.selectPicture(eventId!!,photoId!!)
            }
            .setNeutralButton(
                getString(R.string.i_prefer_not_to)
            ) { _, _ ->
            }
        dialog.create()
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                activity?.onBackPressed()
                true
            }R.id.photo_details_menu_infos->{

                true
            }R.id.photo_detail_menu_delete_photo->{
                deletePicture()
                true
            }
            else->{
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param PHOTO_URL Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotoDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(photoUrl: String = "",photoId:String = "", eventObjectId:String = ""): PhotoDetailFragment {
            val fragment = PhotoDetailFragment()
            val args = Bundle()
            args.putString(PHOTO_URL, photoUrl)
            args.putString(PHOTO_ID, photoId)
            args.putString(EVENT_ID, eventObjectId)
            fragment.arguments = args
            return fragment
        }
    }
}
