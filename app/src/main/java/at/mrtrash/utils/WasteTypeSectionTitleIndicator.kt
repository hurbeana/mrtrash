package at.mrtrash.utils

import android.content.Context
import android.util.AttributeSet
import at.mrtrash.models.WasteType
import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator

/**
 * Indicator for sections of type [WasteType]
 */
internal class WasteTypeSectionTitleIndicator : SectionTitleIndicator<String> {
    override fun setSection(`object`: String?) {
        setTitleText(`object`)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    //override fun setSection(wasteType: WasteType) {
        // Example of using a single character
     //   setTitleText(wasteType.type[0] + "")

        // Example of using a longer string
        // setTitleText(colorGroup.getName());

        //setIndicatorTextColor(colorGroup.getAsColor());
    //}

}
