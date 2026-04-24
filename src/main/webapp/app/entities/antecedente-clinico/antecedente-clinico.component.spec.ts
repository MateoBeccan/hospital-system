import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AntecedenteClinicoService from './antecedente-clinico.service';
import AntecedenteClinico from './antecedente-clinico.vue';

type AntecedenteClinicoComponentType = InstanceType<typeof AntecedenteClinico>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('AntecedenteClinico Management Component', () => {
    let antecedenteClinicoServiceStub: SinonStubbedInstance<AntecedenteClinicoService>;
    let mountOptions: MountingOptions<AntecedenteClinicoComponentType>['global'];

    beforeEach(() => {
      antecedenteClinicoServiceStub = sinon.createStubInstance<AntecedenteClinicoService>(AntecedenteClinicoService);
      antecedenteClinicoServiceStub.retrieve.resolves({ headers: {} });

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
          antecedenteClinicoService: () => antecedenteClinicoServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        antecedenteClinicoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(AntecedenteClinico, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(antecedenteClinicoServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.antecedenteClinicos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(AntecedenteClinico, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(antecedenteClinicoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: AntecedenteClinicoComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(AntecedenteClinico, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        antecedenteClinicoServiceStub.retrieve.reset();
        antecedenteClinicoServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        antecedenteClinicoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(antecedenteClinicoServiceStub.retrieve.called).toBeTruthy();
        expect(comp.antecedenteClinicos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(antecedenteClinicoServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        antecedenteClinicoServiceStub.retrieve.reset();
        antecedenteClinicoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(antecedenteClinicoServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.antecedenteClinicos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(antecedenteClinicoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        antecedenteClinicoServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAntecedenteClinico();
        await comp.$nextTick(); // clear components

        // THEN
        expect(antecedenteClinicoServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(antecedenteClinicoServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
