import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TipoDiagnosticoService from './tipo-diagnostico.service';
import TipoDiagnostico from './tipo-diagnostico.vue';

type TipoDiagnosticoComponentType = InstanceType<typeof TipoDiagnostico>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('TipoDiagnostico Management Component', () => {
    let tipoDiagnosticoServiceStub: SinonStubbedInstance<TipoDiagnosticoService>;
    let mountOptions: MountingOptions<TipoDiagnosticoComponentType>['global'];

    beforeEach(() => {
      tipoDiagnosticoServiceStub = sinon.createStubInstance<TipoDiagnosticoService>(TipoDiagnosticoService);
      tipoDiagnosticoServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          tipoDiagnosticoService: () => tipoDiagnosticoServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tipoDiagnosticoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(TipoDiagnostico, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(tipoDiagnosticoServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.tipoDiagnosticos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(TipoDiagnostico, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(tipoDiagnosticoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: TipoDiagnosticoComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(TipoDiagnostico, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        tipoDiagnosticoServiceStub.retrieve.reset();
        tipoDiagnosticoServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        tipoDiagnosticoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(tipoDiagnosticoServiceStub.retrieve.called).toBeTruthy();
        expect(comp.tipoDiagnosticos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(tipoDiagnosticoServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        tipoDiagnosticoServiceStub.retrieve.reset();
        tipoDiagnosticoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(tipoDiagnosticoServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.tipoDiagnosticos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(tipoDiagnosticoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        tipoDiagnosticoServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeTipoDiagnostico();
        await comp.$nextTick(); // clear components

        // THEN
        expect(tipoDiagnosticoServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(tipoDiagnosticoServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
