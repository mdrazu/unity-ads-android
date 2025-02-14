package com.unity3d.scar.adapter.v2100.signals;

import android.content.Context;

import com.google.android.gms.ads.AdFormat;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.query.QueryInfo;
import com.unity3d.scar.adapter.common.DispatchGroup;
import com.unity3d.scar.adapter.common.signals.ISignalsCollector;
import com.unity3d.scar.adapter.common.signals.SignalCallbackListener;
import com.unity3d.scar.adapter.common.signals.SignalsCollectorBase;
import com.unity3d.scar.adapter.common.signals.SignalsResult;
import com.unity3d.scar.adapter.v2100.requests.AdRequestFactory;

public class SignalsCollector extends SignalsCollectorBase implements ISignalsCollector {
	private AdRequestFactory _adRequestFactory;

	public SignalsCollector(AdRequestFactory adRequestFactory) {
		_adRequestFactory = adRequestFactory;
	}

	@Override
	public void getSCARSignal(final Context context, final String placementId, final boolean isInterstitial, final DispatchGroup dispatchGroup, final SignalsResult signalsResult) {
		AdRequest request = _adRequestFactory.buildAdRequest();
		AdFormat adFormat = isInterstitial ? AdFormat.INTERSTITIAL : AdFormat.REWARDED;
		QueryInfoCallback queryInfoCallback = new QueryInfoCallback(placementId, new SignalCallbackListener(dispatchGroup, signalsResult));
		QueryInfo.generate(context, adFormat, request, queryInfoCallback);
	}

	@Override
	public void getSCARSignal(Context context, boolean isInterstitial, DispatchGroup dispatchGroup, SignalsResult signalsResult) {
		getSCARSignal(context,
			// this will act as the tag
			isInterstitial ? SignalsCollectorBase.SCAR_INT_SIGNAL : SignalsCollectorBase.SCAR_RV_SIGNAL,
			isInterstitial, dispatchGroup, signalsResult
		);
	}
}
