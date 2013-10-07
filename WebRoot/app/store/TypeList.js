/*
 * File: app/store/TypeList.js
 *
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('ForecastingSystem.store.TypeList', {
    extend: 'Ext.data.Store',

    requires: [
        'ForecastingSystem.model.ForecastType'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            model: 'ForecastingSystem.model.ForecastType',
            storeId: 'typeList',
            data: [
                {
                    typeID: 1,
                    typeName: 'Available Service'
                },
                {
                    typeID: 2,
                    typeName: 'Available Skillset'
                },
                {
                    typeID: 3,
                    typeName: 'Available Service Combination'
                },
                {
                    typeID: 4,
                    typeName: 'Available Skillset Combination'
                },
                {
                    typeID: 5,
                    typeName: 'Service With Priority 1'
                },
                {
                    typeID: 6,
                    typeName: 'Service With Priority 2'
                },
                {
                    typeID: 7,
                    typeName: 'Service With Priority 3'
                },
                {
                    typeID: 8,
                    typeName: 'Skillset With Priority 1'
                },
                {
                    typeID: 9,
                    typeName: 'Skillset With Priority 2'
                },
                {
                    typeID: 10,
                    typeName: 'Skillset With Priority 3'
                },
                {
                    typeID: 11,
                    typeName: 'Service Combination With Priority 1'
                },
                {
                    typeID: 12,
                    typeName: 'Service Combination With Priority 2'
                },
                {
                    typeID: 13,
                    typeName: 'Service Combination With Priority 3'
                },
                {
                    typeID: 14,
                    typeName: 'Skillset Combination With Priority 1'
                },
                {
                    typeID: 15,
                    typeName: 'Skillset Combination With Priority 2'
                },
                {
                    typeID: 16,
                    typeName: 'Skillset Combination With Priority 3'
                },
                {
                    typeID: 17,
                    typeName: 'Disabled Service'
                },
                {
                    typeID: 18,
                    typeName: 'Disabled Skillset'
                },
                {
                    typeID: 19,
                    typeName: 'Disabled Service Combination'
                },
                {
                    typeID: 20,
                    typeName: 'Disabled Skillset Combination'
                }
            ]
        }, cfg)]);
    }
});