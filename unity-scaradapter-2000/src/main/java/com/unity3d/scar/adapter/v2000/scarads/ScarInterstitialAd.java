package com.unity3d.scar.adapter.v2000.scarads;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.query.QueryInfo;
import com.unity3d.scar.adapter.common.GMAAdsError;
import com.unity3d.scar.adapter.common.IAdsErrorHandler;
import com.unity3d.scar.adapter.common.IScarInterstitialAdListenerWrapper;
import com.unity3d.scar.adapter.common.scarads.IScarLoadListener;
import com.unity3d.scar.adapter.common.scarads.ScarAdMetadata;

public class ScarInterstitialAd extends ScarAdBase<InterstitialAd> {

	public ScarInterstitialAd(Context context, QueryInfo queryInfo, ScarAdMetadata scarAdMetadata, IAdsErrorHandler adsErrorHandler, IScarInterstitialAdListenerWrapper adListener) {
		super(context, scarAdMetadata, queryInfo, adsErrorHandler);
		_scarAdListener = new ScarInterstitialAdListener(adListener, this);
	}

	@Override
	protected void loadAdInternal(AdRequest adRequest, IScarLoadListener loadListener) {
		InterstitialAd.load(_context, _scarAdMetadata.getAdUnitId(), adRequest, ((ScarInterstitialAdListener)_scarAdListener).getAdLoadListener());
	}

	@Override
	public void show(Activity activity) {
		if (_adObj != null) {
			_adObj.show(activity);
		} else {
			_adsErrorHandler.handleError(GMAAdsError.AdNotLoadedError(_scarAdMetadata));
		}
	}
}
