package us.mn.state.health.model.purchasing

/**
 * User: kiminn1
 * Date: 7/12/2017
 * Time: 5:16 PM
 */

enum AssetType {

    CAMERA('Camera', 'Camera, video camera, or video conference camera if $500 or more', 'MDH'),
    LABEQUIPMENT('Lab Equipment', 'Lab equipment costing $5000 or more', 'MDH'),
    TV('Television', 'A Television or wall-mounted flatscreen monitor if over $500', 'MDH'),
    RADIO('Radio', 'A handheld radio device for communication', 'MDH'),
    PORTABLESCANNER('Portable Scanner', 'Portable Document Scanner', 'MDH'),
    PORTABLEPROJECTOR('Portable Projector', 'Portable Projector', 'MDH'),
    OTHER('Other-MDH','Any other assets purchased by MDH costing $5000 or more', 'MDH'),

    TELECOM('Telecom Equipment', 'All VOIP phones and teleconference equipment', 'MNIT'),
    PCDESKTOP('Desktop Computer','A Desktop Computer', 'MNIT'),
    PCLAPTOP('Mobile Computer', 'Laptop or Tablet Computer, Includes E-readers', 'MNIT'),
    PORTABLEPRINTER('Portable Printer', 'Portable Printer', 'MNIT'),
    SERVER('Server', 'Computer Server', 'MNIT'),
    OTHERIT('Other-IT','Any other assets purchased by MN.IT for use at MDH costing $5000 or more', 'MNIT'),
    NONASSET('Not an Asset', 'Does not meet asset criteria', 'MDH')

    String name //For JSP Access
    String label
    String description
    String org

    AssetType(String label, String description, String org) {
        this.name = this.name()
        this.label = label
        this.description = description
        this.org = org
    }

    static List<AssetType> getAssetTypes(){
        AssetType.values().toList().sort {it.label}
    }

    static List<AssetType> getMdhAssetTypes(){
        AssetType.values().findAll {it.org == 'MDH'}.toList()
    }

    static List<AssetType> getMnitAssetTypes(){
        AssetType.values().findAll {it.org == 'MNIT'}.toList()
    }
}