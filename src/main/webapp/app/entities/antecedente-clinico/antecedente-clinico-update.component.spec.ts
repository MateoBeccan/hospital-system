import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import HistoriaClinicaService from '@/entities/historia-clinica/historia-clinica.service';
import AlertService from '@/shared/alert/alert.service';

import AntecedenteClinicoUpdate from './antecedente-clinico-update.vue';
import AntecedenteClinicoService from './antecedente-clinico.service';

type AntecedenteClinicoUpdateComponentType = InstanceType<typeof AntecedenteClinicoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const antecedenteClinicoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AntecedenteClinicoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('AntecedenteClinico Management Update Component', () => {
    let comp: AntecedenteClinicoUpdateComponentType;
    let antecedenteClinicoServiceStub: SinonStubbedInstance<AntecedenteClinicoService>;

    beforeEach(() => {
      route = {};
      antecedenteClinicoServiceStub = sinon.createStubInstance<AntecedenteClinicoService>(AntecedenteClinicoService);
      antecedenteClinicoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          antecedenteClinicoService: () => antecedenteClinicoServiceStub,
          historiaClinicaService: () =>
            sinon.createStubInstance<HistoriaClinicaService>(HistoriaClinicaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(AntecedenteClinicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.antecedenteClinico = antecedenteClinicoSample;
        antecedenteClinicoServiceStub.update.resolves(antecedenteClinicoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(antecedenteClinicoServiceStub.update.calledWith(antecedenteClinicoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        antecedenteClinicoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AntecedenteClinicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.antecedenteClinico = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(antecedenteClinicoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        antecedenteClinicoServiceStub.find.resolves(antecedenteClinicoSample);
        antecedenteClinicoServiceStub.retrieve.resolves([antecedenteClinicoSample]);

        // WHEN
        route = {
          params: {
            antecedenteClinicoId: `${antecedenteClinicoSample.id}`,
          },
        };
        const wrapper = shallowMount(AntecedenteClinicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.antecedenteClinico).toMatchObject(antecedenteClinicoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        antecedenteClinicoServiceStub.find.resolves(antecedenteClinicoSample);
        const wrapper = shallowMount(AntecedenteClinicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
