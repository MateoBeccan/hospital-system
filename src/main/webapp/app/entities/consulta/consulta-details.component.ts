import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IConsulta } from '@/shared/model/consulta.model';

import ConsultaService from './consulta.service';

export default defineComponent({
  name: 'ConsultaDetails',
  setup() {
    const dateFormat = useDateFormat();
    const consultaService = inject('consultaService', () => new ConsultaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const consulta: Ref<IConsulta> = ref({});

    const retrieveConsulta = async consultaId => {
      try {
        const res = await consultaService().find(consultaId);
        consulta.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.consultaId) {
      retrieveConsulta(route.params.consultaId);
    }

    return {
      ...dateFormat,
      alertService,
      consulta,

      ...dataUtils,

      previousState,
    };
  },
});
